package de.mytoysgroup.movies.challenge.common.di

import android.content.Context
import com.squareup.moshi.Moshi
import de.mytoysgroup.movies.challenge.common.data.local.SharedPreferencesDataSource
import de.mytoysgroup.movies.challenge.common.data.remote.OmdbApi
import de.mytoysgroup.movies.challenge.common.data.remote.OmdbDataSource
import de.mytoysgroup.movies.challenge.common.domain.repository.MoviesLocalRepository
import de.mytoysgroup.movies.challenge.common.domain.repository.MoviesRemoteRepository
import de.mytoysgroup.movies.challenge.common.navigator.MovieAppNavigator
import de.mytoysgroup.movies.challenge.common.navigator.Navigator
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

fun appModule(context: Context) = Kodein.Module {
  bind<Context>() with provider { context }
  bind<Navigator>() with provider { MovieAppNavigator(instance()) }
  bind<Scheduler>(tag = "bg") with singleton { Schedulers.io() }
  bind<Scheduler>(tag = "main") with singleton { AndroidSchedulers.mainThread() }

  bind<Moshi>() with singleton { Moshi.Builder().build() }
  bind<OmdbApi>() with singleton {
    Retrofit.Builder()
      .baseUrl("http://www.omdbapi.com")
      .addConverterFactory(MoshiConverterFactory.create(instance()))
      .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
      .build()
      .create(OmdbApi::class.java)
  }

  bind<MoviesRemoteRepository>() with singleton { OmdbDataSource(instance()) }

  bind<MoviesLocalRepository>() with singleton { SharedPreferencesDataSource(instance(), instance()) }
}
