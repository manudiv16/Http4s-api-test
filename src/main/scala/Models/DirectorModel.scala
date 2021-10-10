package Models
import scala.collection.mutable

object DirecectorModel {

  type Id = String

  case class Director(firstName: String, lastName: String) {
    override def toString: Id = s"$firstName $lastName"
  }

  case class DirectorDetails(
      firstName: String,
      lastName: String,
      genere: String
  )

  val directorDetailsDB: mutable.Map[Director, DirectorDetails] =
    mutable.Map(
      Director("Zack", "Snyder") -> DirectorDetails(
        "Zack",
        "Snyder",
        "puto flipado"
      )
    )
}
