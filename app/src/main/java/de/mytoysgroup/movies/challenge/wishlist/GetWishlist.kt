package de.mytoysgroup.movies.challenge.wishlist

import de.mytoysgroup.movies.challenge.common.domain.model.Movie
import de.mytoysgroup.movies.challenge.common.domain.repository.MoviesLocalRepository
import de.mytoysgroup.movies.challenge.common.domain.usecase.UseCase
import io.reactivex.Single

class GetWishlist(
  private val moviesLocalRepository: MoviesLocalRepository
) : UseCase<Unit, Single<List<Movie>>>() {

  override fun execute(params: Unit?): Single<List<Movie>> = moviesLocalRepository.getAll()


}