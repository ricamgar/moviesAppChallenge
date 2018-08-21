package de.mytoysgroup.movies.challenge.common.domain.model

data class Movie(
  val title: String,
  val year: String,
  val id: String,
  val poster: String,
  val director: String = "",
  val actors: String = "",
  val plot: String = "")