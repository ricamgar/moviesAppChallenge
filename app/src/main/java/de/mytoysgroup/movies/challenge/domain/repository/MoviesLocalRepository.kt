package de.mytoysgroup.movies.challenge.domain.repository

import de.mytoysgroup.movies.challenge.domain.model.Movie
import io.reactivex.Completable
import io.reactivex.Single

interface MoviesLocalRepository {
  fun getAll(): Single<List<Movie>>
  fun save(movie: Movie): Completable
}