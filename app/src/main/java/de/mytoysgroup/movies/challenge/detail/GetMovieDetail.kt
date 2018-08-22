package de.mytoysgroup.movies.challenge.detail

import de.mytoysgroup.movies.challenge.common.domain.model.Movie
import de.mytoysgroup.movies.challenge.common.domain.repository.MoviesRemoteRepository
import de.mytoysgroup.movies.challenge.common.domain.usecase.UseCase
import io.reactivex.Single

class GetMovieDetail(
  private val moviesRemoteRepository: MoviesRemoteRepository
) : UseCase<String, Single<Movie>>() {

  override fun execute(params: String?): Single<Movie> =
    moviesRemoteRepository.get(params!!)
}