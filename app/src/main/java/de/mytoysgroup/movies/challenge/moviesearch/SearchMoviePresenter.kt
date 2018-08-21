package de.mytoysgroup.movies.challenge.moviesearch

import de.mytoysgroup.movies.challenge.common.domain.model.Movie
import de.mytoysgroup.movies.challenge.common.presenter.BasePresenter
import io.reactivex.Observable
import io.reactivex.Scheduler

class SearchMoviePresenter(
  private val searchMovie: SearchMovie,
  backgroundThread: Scheduler,
  mainThread: Scheduler
) : BasePresenter<SearchMoviePresenter.View>(backgroundThread, mainThread) {

  override fun resume(view: View) {
    super.resume(view)
    subscribeToSearchStream()
  }

  private fun subscribeToSearchStream() {
    disposables.add(view!!.searchStream
      .flatMap { searchMovie.execute(SearchMovie.Params(it)).toObservable() }
      .compose(observableSchedulers())
      .subscribe(
        { view?.showMovies(it) },
        { view?.showError(it) }))
  }

  interface View : BasePresenter.View {
    var searchStream: Observable<String>
    fun showMovies(movies: List<Movie>)
  }
}