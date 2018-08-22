package de.mytoysgroup.movies.challenge.detail

import de.mytoysgroup.movies.challenge.wishlist.GetWishlist
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

fun movieDetailModule(id: String) = Kodein.Module {
  bind<GetMovieDetail>() with provider { GetMovieDetail(instance()) }
  bind<GetWishlist>() with provider { GetWishlist(instance()) }
  bind<SaveMovie>() with provider { SaveMovie(instance()) }
  bind<RemoveMovie>() with provider { RemoveMovie(instance()) }
  bind<MovieDetailPresenter>() with provider {
    MovieDetailPresenter(id, instance(), instance(), instance(), instance(),
      instance(tag = "bg"), instance(tag = "main"))
  }
}
