package de.mytoysgroup.movies.challenge.detail

import de.mytoysgroup.movies.challenge.common.domain.model.Movie
import de.mytoysgroup.movies.challenge.common.domain.repository.MoviesLocalRepository
import de.mytoysgroup.movies.challenge.common.domain.usecase.UseCase
import io.reactivex.Completable

class SaveMovie(
  private val moviesLocalRepository: MoviesLocalRepository
) : UseCase<Movie, Completable>() {

  override fun execute(params: Movie?): Completable {
    return moviesLocalRepository.save(params!!)
  }
}