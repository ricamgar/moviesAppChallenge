package de.mytoysgroup.movies.challenge.moviesearch

import de.mytoysgroup.movies.challenge.common.domain.model.Movie
import de.mytoysgroup.movies.challenge.common.navigator.Navigator
import de.mytoysgroup.movies.challenge.common.presenter.BasePresenter
import io.reactivex.Observable
import io.reactivex.Scheduler
import java.util.concurrent.TimeUnit

class SearchMoviePresenter(
  private val searchMovie: SearchMovie,
  private val navigator: Navigator,
  private val computationThread: Scheduler,
  backgroundThread: Scheduler,
  private val mainThread: Scheduler
) : BasePresenter<SearchMoviePresenter.View>(backgroundThread, mainThread) {

  override fun resume(view: View) {
    super.resume(view)
    subscribeToSearchStream()
    subscribeToClearClicked()
  }

  private fun subscribeToSearchStream() {
    disposables.add(view!!.searchStream
      .observeOn(mainThread)
      .doOnNext {
        view?.setLoadingVisibility(true)
        view?.enableClear(true)
      }
      .debounce(1, TimeUnit.SECONDS, computationThread)
      .flatMapSingle { searchMovie.execute(SearchMovie.Params(it)) }
      .compose(observableSchedulers())
      .subscribe(
        {
          if (it.isEmpty()) {
            view?.showEmpty()
          } else {
            view?.showMovies(it)
          }
          view?.setLoadingVisibility(false)
        },
        { view?.showError(it) }))
  }

  private fun subscribeToClearClicked() {
    disposables.add(view!!.clearStream
      .subscribe {
        view?.clearResults()
      })
  }

  fun movieClicked(id: String) {
    navigator.goToDetail(id)
  }

  interface View : BasePresenter.View {
    val searchStream: Observable<String>
    val clearStream: Observable<Any>
    fun showMovies(movies: List<Movie>)
    fun showEmpty()
    fun enableClear(enable: Boolean)
    fun clearResults()
  }
}