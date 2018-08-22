package de.mytoysgroup.movies.challenge.detail

import de.mytoysgroup.movies.challenge.common.domain.model.Movie
import de.mytoysgroup.movies.challenge.common.presenter.BasePresenter
import io.reactivex.Scheduler

class MovieDetailPresenter(
  private val id: String,
  private val getMovieDetail: GetMovieDetail,
  private val saveMovie: SaveMovie,
  backgroundThread: Scheduler,
  mainThread: Scheduler
) : BasePresenter<MovieDetailPresenter.View>(backgroundThread, mainThread) {

  override fun resume(view: View) {
    super.resume(view)
    getMovieDetail()
  }

  private fun getMovieDetail() {
    disposables.add(getMovieDetail.execute(id)
      .compose(singleSchedulers())
      .subscribe(
        { view?.showDetails(it) },
        { view?.showError(it) }))
  }

  interface View : BasePresenter.View {
    fun showDetails(movie: Movie)
  }
}