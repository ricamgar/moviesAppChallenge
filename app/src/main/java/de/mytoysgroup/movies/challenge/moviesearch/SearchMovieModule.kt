package de.mytoysgroup.movies.challenge.moviesearch

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

fun searchMovieModule() = Kodein.Module {
  bind<SearchMovie>() with provider { SearchMovie(instance()) }
  bind<Scheduler>(tag = "computation") with provider { Schedulers.computation() }
  bind<SearchMoviePresenter>() with provider {
    SearchMoviePresenter(instance(), instance(tag = "computation"),
      instance(tag = "bg"), instance(tag = "main"))
  }
}