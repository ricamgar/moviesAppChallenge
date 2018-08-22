package de.mytoysgroup.movies.challenge.moviesearch

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.GridLayoutManager
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import de.mytoysgroup.movies.challenge.R
import de.mytoysgroup.movies.challenge.common.di.InjectedActivity
import de.mytoysgroup.movies.challenge.common.domain.model.Movie
import de.mytoysgroup.movies.challenge.common.util.EndlessRecyclerOnScrollListener
import de.mytoysgroup.movies.challenge.common.util.visibility
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_search_movie.*
import org.kodein.di.Kodein
import org.kodein.di.generic.instance

class SearchMovieActivity : InjectedActivity(), SearchMoviePresenter.View {

  private val presenter: SearchMoviePresenter by instance()
  private val adapter = SearchMovieAdapter()
  private lateinit var scrollListener: EndlessRecyclerOnScrollListener

  override fun activityModule() = Kodein.Module {
    import(searchMovieModule())
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_search_movie)
    setSupportActionBar(toolbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    setupMoviesResultsList()
    presenter.attachView(this)
  }

  override fun onDestroy() {
    super.onDestroy()
    presenter.detachView()
  }

  private fun setupMoviesResultsList() {
    resultsList.setHasFixedSize(true)
    val layoutManager = GridLayoutManager(this, 2)
    resultsList.layoutManager = layoutManager
    resultsList.adapter = adapter
    adapter.onItemClick = ::onMovieClick
    scrollListener = object : EndlessRecyclerOnScrollListener(layoutManager, 5) {
      override fun onLoadMore(currentPage: Int) {
        presenter.nextPage(currentPage)
        hideKeyboard()
      }
    }
    resultsList.addOnScrollListener(scrollListener)
  }

  private fun onMovieClick(id: String) {
    presenter.movieClicked(id)
  }

  private fun resetSearch(it: String) {
    enableClear(it.isNotEmpty())
    scrollListener.reset(0, true)
    adapter.clear()
    resultsList.scrollToPosition(0)
    if (it.isNotEmpty()) presenter.nextPage(1)
  }

  override val searchStream: Observable<String>
    get() = RxTextView.afterTextChangeEvents(searchEdit)
      .map { it.editable().toString() }.doOnNext {
        resetSearch(it)
      }.filter { it.isNotEmpty() }

  override val clearStream: Observable<Any>
    get() = RxView.clicks(clearSearch)

  override fun showMovies(movies: List<Movie>) {
    adapter.addMovies(movies)
    empty.visibility(false)
    resultsList.visibility(true)
  }

  override fun showEmpty() {
    if (adapter.itemCount == 0) {
      empty.visibility(true)
      resultsList.visibility(false)
    }
  }

  override fun setLoadingVisibility(visible: Boolean) {
    loading.visibility(visible)
  }

  override fun enableClear(enable: Boolean) {
    clearSearch.isEnabled = enable
  }

  override fun clearResults() {
    searchEdit.text.clear()
    empty.visibility(false)
    resultsList.visibility(false)
    enableClear(false)
  }

  override fun showError(error: Throwable) {
    super.showError(error)
    Snackbar.make(findViewById(android.R.id.content),
      "Error: ${error.message}", Snackbar.LENGTH_LONG).show()
  }

  companion object {
    fun getIntent(context: Context): Intent =
      Intent(context, SearchMovieActivity::class.java)
  }
}
