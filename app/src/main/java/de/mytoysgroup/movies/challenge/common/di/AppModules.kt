package de.mytoysgroup.movies.challenge.common.di

import android.content.Context
import com.squareup.moshi.Moshi
import de.mytoysgroup.movies.challenge.common.data.local.SharedPreferencesDataSource
import de.mytoysgroup.movies.challenge.common.domain.model.Movie
import de.mytoysgroup.movies.challenge.common.domain.repository.MoviesLocalRepository
import de.mytoysgroup.movies.challenge.common.domain.repository.MoviesRemoteRepository
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

fun appModule(context: Context) = Kodein.Module {
  bind<Context>() with provider { context }
  bind<Scheduler>(tag = "bg") with singleton { Schedulers.io() }
  bind<Scheduler>(tag = "main") with singleton { AndroidSchedulers.mainThread() }
  bind<Moshi>() with singleton { Moshi.Builder().build() }

  bind<MoviesRemoteRepository>() with singleton {
    object : MoviesRemoteRepository {
      override fun search(searchTerm: String, page: Int): Single<List<Movie>> {
        TODO(
          "not implemented") //To change body of created functions use File | Settings | File Templates.
      }

      override fun get(id: String): Single<Movie> {
        TODO(
          "not implemented") //To change body of created functions use File | Settings | File Templates.
      }
    }
  }

  bind<MoviesLocalRepository>() with singleton { SharedPreferencesDataSource(instance(), instance()) }
}
