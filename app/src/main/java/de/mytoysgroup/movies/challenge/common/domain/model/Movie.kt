package de.mytoysgroup.movies.challenge.common.domain.model

import com.squareup.moshi.Json

data class Movie(
  @field:Json(name = "Title") val title: String,
  @field:Json(name = "Year") val year: String,
  @field:Json(name = "imdbID") val id: String,
  @field:Json(name = "Poster") val poster: String,
  @field:Json(name = "Director") val director: String = "",
  @field:Json(name = "Actors") val actors: String = "",
  @field:Json(name = "Plot") val plot: String = "",
  @field:Json(name = "Runtime") val runtime: String = "",
  @field:Json(name = "Country") val country: String = "")