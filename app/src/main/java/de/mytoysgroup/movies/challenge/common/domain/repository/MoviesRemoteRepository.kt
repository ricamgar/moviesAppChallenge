package de.mytoysgroup.movies.challenge.common.domain.repository

import de.mytoysgroup.movies.challenge.common.domain.model.Movie
import io.reactivex.Single

interface MoviesRemoteRepository {
  fun search(searchTerm: String, page: Int): Single<List<Movie>>
  fun get(id: String): Single<Movie>
}