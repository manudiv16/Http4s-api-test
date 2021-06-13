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
import java.util.UUID
import scala.util.Try
import scala.collection.mutable
import scala.collection.mutable
import scala.util.Try

object DirectorRoute {

  type Id = String

  case class Director(firstName: String, lastName: String) {
    override def toString: Id = s"$firstName $lastName"
  }

  case class DirectorDetails(
      firstName: String,
      lastName: String,
      genere: String
  )

  object DirectorPath {
    def unapply(str: String): Option[Director] = {
      Try {
        val tokens = str.split(" ")
        Director(tokens(0), tokens(1))
      }.toOption
    }
  }

  val directorDetailsDB: mutable.Map[Director, DirectorDetails] =
    mutable.Map(
      Director("Zack", "Snyder") -> DirectorDetails(
        "Zack",
        "Snyder",
        "puto flipado"
      )
    )

  def directorRoutes[F[_]: Monad]: HttpRoutes[F] = {
    val dsl = Http4sDsl[F]
    import dsl._

    HttpRoutes.of[F] {
      case GET -> Root / "directors" / DirectorPath(director) =>
        directorDetailsDB.get(director) match {
          case Some(directorDi) => Ok(directorDi.asJson)
          case _                => NotFound(s"No director '$director' found")
        }
    }
  }
}
