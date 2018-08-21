package de.mytoysgroup.movies.challenge.domain.repository

import de.mytoysgroup.movies.challenge.domain.model.Movie
import io.reactivex.Single

interface MoviesRemoteRepository {
  fun search(searchTerm: String): Single<List<Movie>>
  fun get(id: String): Single<Movie>
}