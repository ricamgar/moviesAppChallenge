package de.mytoysgroup.movies.challenge.common.data.remote

import de.mytoysgroup.movies.challenge.common.domain.model.Movie
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface OmdbApi {
  @GET("/")
  fun search(
    @Query("s") searchTerm: String,
    @Query("page") page: Int = 1,
    @Query("apikey") apikey: String = "d8ebe432"): Single<SearchResponse>

  @GET("/")
  fun getMovie(
    @Query("i") id: String,
    @Query("apikey") apikey: String = "d8ebe432"): Single<Movie>
}