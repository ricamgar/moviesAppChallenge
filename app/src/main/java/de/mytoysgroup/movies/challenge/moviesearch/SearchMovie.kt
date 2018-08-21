package de.mytoysgroup.movies.challenge.moviesearch

import de.mytoysgroup.movies.challenge.common.domain.model.Movie
import de.mytoysgroup.movies.challenge.common.domain.repository.MoviesRemoteRepository
import de.mytoysgroup.movies.challenge.common.domain.usecase.UseCase
import io.reactivex.Single

class SearchMovie(
  private val moviesRemoteRepository: MoviesRemoteRepository
) : UseCase<SearchMovie.Params, Single<List<Movie>>>() {

  override fun execute(params: Params): Single<List<Movie>> =
    moviesRemoteRepository.search(params.searchTerm, params.page)

  data class Params(val searchTerm: String, val page: Int = 0)
}