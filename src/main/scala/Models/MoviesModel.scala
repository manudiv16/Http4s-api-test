package Models

import java.util.UUID

object MoviesModel {

  type Actor = String
  type Id = String

  case class Movie(
      id: Id,
      title: String,
      year: Int,
      actors: List[Actor],
      director: String
  )

  val snjl: Movie = Movie(
    "6bcbca1e-efd3-411d-9f7c-14b872444fce",
    "Zack Snyder's Justice League",
    2021,
    List(
      "Henry Cavill",
      "Gal Godot",
      "Ezra Miller",
      "Ben Affleck",
      "Ray Fisher",
      "Jason Momoa"
    ),
    "Zack Snyder"
  )

  val movies: Map[String, Movie] = Map(snjl.id -> snjl)

  def findMovieById(movieId: UUID) =
    movies.get(movieId.toString)

  def findMoviesByDirector(director: Option[String]): List[Movie] =
    director match {
      case Some(a) => movies.values.filter(_.director == a).toList
      case None    => movies.values.toList
    }
}
