import cats._
import cats.effect._
import cats.implicits._
import org.http4s._
import org.http4s.headers._
import org.http4s.implicits._
import org.http4s.server._
import org.http4s.blaze.server._
import Routes.{MoviesRoute, DirectorRoute}

object Http4sTutorial extends IOApp {

  def allRoutes[F[_]: Monad]: HttpRoutes[F] =
    MoviesRoute.movieRoutes[F] <+> DirectorRoute
      .directorRoutes[F] // cats.syntax.semigroupk._

  def allRoutesComplete[F[_]: Monad]: HttpApp[F] =
    allRoutes[F].orNotFound

  def run(args: List[String]): IO[ExitCode] = {

    val apis = Router(
      "/api" -> MoviesRoute.movieRoutes[IO],
      "/api/private" -> DirectorRoute.directorRoutes[IO]
    ).orNotFound

    BlazeServerBuilder[IO](runtime.compute)
      .bindHttp(8080, "localhost")
      .withHttpApp(apis)
      .resource
      .use(_ => IO.never)
      .as(ExitCode.Success)
  }
}
