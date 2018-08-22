package de.mytoysgroup.movies.challenge.detail

import de.mytoysgroup.movies.challenge.common.domain.model.Movie
import de.mytoysgroup.movies.challenge.common.presenter.BasePresenter
import de.mytoysgroup.movies.challenge.wishlist.GetWishlist
import io.reactivex.Scheduler
import io.reactivex.functions.BiFunction

class MovieDetailPresenter(
  private val id: String,
  private val getMovieDetail: GetMovieDetail,
  private val getWishlist: GetWishlist,
  private val saveMovie: SaveMovie,
  private val removeMovie: RemoveMovie,
  backgroundThread: Scheduler,
  mainThread: Scheduler
) : BasePresenter<MovieDetailPresenter.View>(backgroundThread, mainThread) {

  override fun attachView(view: View) {
    super.attachView(view)
    getMovieDetail()
  }

  private fun getMovieDetail() {
    disposables.add(getMovieDetail.execute(id)
      .zipWith(getWishlist.execute(),
        BiFunction<Movie, List<Movie>, Pair<Movie, Boolean>> { movie, wishlist ->
          val isFavorite = wishlist.any { it.id == movie.id }
          movie to isFavorite
        })
      .compose(singleSchedulers())
      .subscribe(
        { result -> view?.showDetails(result.first, result.second) },
        { view?.showError(it) }))
  }

  fun addClicked(movie: Movie) {
    disposables.add(saveMovie.execute(movie)
      .compose(completableSchedulers())
      .subscribe { view?.showDetails(movie, true) })
  }

  fun removeClicked(movie: Movie) {
    disposables.add(removeMovie.execute(movie)
      .compose(completableSchedulers())
      .subscribe { view?.showDetails(movie, false) })
  }


  interface View : BasePresenter.View {
    fun showDetails(movie: Movie, isFavorite: Boolean)
  }
}