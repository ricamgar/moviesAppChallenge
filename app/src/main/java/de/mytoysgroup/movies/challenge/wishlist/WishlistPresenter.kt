package de.mytoysgroup.movies.challenge.wishlist

import de.mytoysgroup.movies.challenge.common.domain.model.Movie
import de.mytoysgroup.movies.challenge.common.presenter.BasePresenter
import io.reactivex.Scheduler

class WishlistPresenter(
  private val getWishlist: GetWishlist,
  backgroundThread: Scheduler,
  mainThread: Scheduler
) : BasePresenter<WishlistPresenter.View>(backgroundThread, mainThread) {

  override fun resume(view: View) {
    super.resume(view)
    getWishlist()
  }

  private fun getWishlist() {
    disposables.add(getWishlist.execute()
      .compose(singleSchedulers())
      .doOnSubscribe { view?.setLoadingVisivility(true) }
      .doFinally { view?.setLoadingVisivility(false) }
      .subscribe(
        { view?.showMovies(it) },
        { view?.showError(it) }))
  }

  interface View : BasePresenter.View {
    fun showMovies(movies: List<Movie>)
  }
}