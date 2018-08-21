package de.mytoysgroup.movies.challenge.common.data.remote

import de.mytoysgroup.movies.challenge.common.domain.model.Movie
import de.mytoysgroup.movies.challenge.common.domain.repository.MoviesRemoteRepository
import io.reactivex.Single

class OmdbDataSource(
  private val omdbApi: OmdbApi
) : MoviesRemoteRepository {

  override fun search(searchTerm: String, page: Int): Single<List<Movie>> {
    return omdbApi.search(searchTerm, page)
      .map { if (it.response == "True") it.search else emptyList() }
  }

  override fun get(id: String): Single<Movie> {
    return omdbApi.getMovie(id)
  }
}
