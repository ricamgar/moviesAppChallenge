package de.mytoysgroup.movies.challenge.wishlist

import de.mytoysgroup.movies.challenge.common.domain.model.Movie
import de.mytoysgroup.movies.challenge.common.navigator.Navigator
import de.mytoysgroup.movies.challenge.common.presenter.BasePresenter
import io.reactivex.Scheduler

class WishlistPresenter(
  private val getWishlist: GetWishlist,
  private val navigator: Navigator,
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
      .doOnSubscribe { view?.setLoadingVisibility(true) }
      .doFinally { view?.setLoadingVisibility(false) }
      .subscribe(
        {
          if (it.isEmpty()) {
            view?.showEmpty()
          } else {
            view?.showMovies(it)
          }
        },
        { view?.showError(it) }))
  }

  fun onSearchClicked() {
    navigator.goToSearch()
  }

  fun movieClicked(id: String) {
    navigator.goToDetail(id)
  }

  interface View : BasePresenter.View {
    fun showMovies(movies: List<Movie>)
    fun showEmpty()
  }
}