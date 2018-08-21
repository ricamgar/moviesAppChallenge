package de.mytoysgroup.movies.challenge.common.data.remote

import com.squareup.moshi.Json
import de.mytoysgroup.movies.challenge.common.domain.model.Movie

data class SearchResponse(
  @field:Json(name = "Search") val search: List<Movie>,
  val totalResults: Int,
  @field:Json(name = "Response") val response: String
)
