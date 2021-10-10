package Routes

import cats._
import cats.implicits._
import org.http4s.circe._
import org.http4s._
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.dsl._
import org.http4s.dsl.impl._
import org.http4s.implicits._
import java.time.Year
import scala.util.Try
import scala.collection.mutable
import Models.MoviesModel._

object MoviesRoute {

  implicit val yearQueryParamDecoder: QueryParamDecoder[Year] =
    QueryParamDecoder[Int].emap(yearInt =>
      Try(Year.of(yearInt)).toEither.leftMap { e =>
        ParseFailure(e.getMessage, e.getMessage)
      }
    )

  object DirectorQueryParamMatcher
      extends OptionalQueryParamDecoderMatcher[String]("director")

  object YearQueryParamMatcher
      extends OptionalValidatingQueryParamDecoderMatcher[Year]("year")

  def movieRoutes[F[_]: Monad]: HttpRoutes[F] = {
    val dsl = Http4sDsl[F]
    import dsl._

    HttpRoutes.of[F] {
      case GET -> Root / "movies" :? DirectorQueryParamMatcher(
            director
          ) +& YearQueryParamMatcher(mayBeYear) =>
        val moviesByDirector = findMoviesByDirector(director)
        mayBeYear match {
          case Some(y) =>
            y.fold(
              _ => BadRequest("the year was badly formatted"),
              year => {
                val moviesByDirectorAndYear =
                  moviesByDirector.filter(_.year == year.getValue)
                Ok(moviesByDirectorAndYear.asJson)
              }
            )
          case None => Ok(moviesByDirector.asJson)
        }
      case GET -> Root / "movies" / UUIDVar(movieId) =>
        findMovieById(movieId) match {
          case Some(actors) => Ok(actors.asJson)
          case _ => NotFound(s"No movie with id $movieId found in the database")
        }
      case GET -> Root / "movies" / UUIDVar(movieId) / "actors" =>
        findMovieById(movieId).map(_.actors) match {
          case Some(actors) => Ok(actors.asJson)
          case _ =>
            NotFound(s"No movie with id $movieId found in the database xd")
        }
    }
  }

}
