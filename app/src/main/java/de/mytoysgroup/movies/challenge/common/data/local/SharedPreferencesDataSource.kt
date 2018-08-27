package de.mytoysgroup.movies.challenge.common.data.local

import android.content.Context
import android.preference.PreferenceManager
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import de.mytoysgroup.movies.challenge.common.domain.model.Movie
import de.mytoysgroup.movies.challenge.common.domain.repository.MoviesLocalRepository
import io.reactivex.Completable
import io.reactivex.Single

class SharedPreferencesDataSource(
  context: Context,
  moshi: Moshi) : MoviesLocalRepository {

  private val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
  private val moviesListType = Types.newParameterizedType(List::class.java, Movie::class.java)
  private val moviesAdapter = moshi.adapter<List<Movie>>(moviesListType)
  private val favoritesKey = "favorites"

  override fun getAll(): Single<List<Movie>> {
    return Single.fromCallable {
      if (sharedPreferences.contains(favoritesKey)) {
        moviesAdapter.fromJson(sharedPreferences.get(favoritesKey, "")!!)
      } else {
        listOf()
      }
    }
  }

  override fun save(movie: Movie): Completable {
    return getAll()
      .map {
        val movies = it.toMutableList()
        movies.add(movie)
        sharedPreferences.set(favoritesKey, moviesAdapter.toJson(movies))
      }.toCompletable()
  }

  override fun remove(movie: Movie): Completable {
    return getAll()
      .map { movies ->
        val mutableList = movies.toMutableList()
        mutableList.removeAll { it.id == movie.id }
        sharedPreferences.set(favoritesKey, moviesAdapter.toJson(mutableList))
      }.toCompletable()
  }

  override fun removeAll(): Completable {
    return Completable.fromCallable {
      sharedPreferences.edit().clear().apply()
    }
  }
}