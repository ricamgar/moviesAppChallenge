package de.mytoysgroup.movies.challenge.wishlist

import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

fun wishlistModule() = Kodein.Module {
  bind<GetWishlist>() with provider { GetWishlist(instance()) }
  bind<WishlistPresenter>() with provider {
    WishlistPresenter(instance(), instance(tag = "bg"), instance(tag = "main"))
  }
}