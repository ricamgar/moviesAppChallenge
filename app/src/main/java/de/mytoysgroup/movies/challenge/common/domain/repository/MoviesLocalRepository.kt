package de.mytoysgroup.movies.challenge.common.domain.repository

import de.mytoysgroup.movies.challenge.common.domain.model.Movie
import io.reactivex.Completable
import io.reactivex.Single

interface MoviesLocalRepository {
  fun getAll(): Single<List<Movie>>
  fun save(movie: Movie): Completable
  fun remove(movie: Movie): Completable
  fun removeAll(): Completable
}