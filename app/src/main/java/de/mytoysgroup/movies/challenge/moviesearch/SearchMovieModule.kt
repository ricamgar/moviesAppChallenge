package de.mytoysgroup.movies.challenge.moviesearch

import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

fun searchMovieModule() = Kodein.Module {
  bind<SearchMovie>() with provider { SearchMovie(instance()) }
  bind<SearchMoviePresenter>() with provider {
    SearchMoviePresenter(instance(), instance(tag = "bg"), instance(tag = "main"))
  }
}