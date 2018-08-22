package de.mytoysgroup.movies.challenge.moviesearch

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import de.mytoysgroup.movies.challenge.R
import de.mytoysgroup.movies.challenge.common.di.InjectedActivity
import de.mytoysgroup.movies.challenge.common.domain.model.Movie
import de.mytoysgroup.movies.challenge.common.util.visibility
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_search_movie.*
import org.kodein.di.Kodein
import org.kodein.di.generic.instance

class SearchMovieActivity : InjectedActivity(), SearchMoviePresenter.View {

  private val presenter: SearchMoviePresenter by instance()
  private val adapter = SearchMovieAdapter()

  override fun activityModule() = Kodein.Module {
    import(searchMovieModule())
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_search_movie)
    setSupportActionBar(toolbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    setupMoviesResultsList()
  }

  override fun onResume() {
    super.onResume()
    presenter.resume(this)
  }

  override fun onPause() {
    super.onPause()
    presenter.pause()
  }

  private fun setupMoviesResultsList() {
    resultsList.setHasFixedSize(true)
    resultsList.layoutManager = LinearLayoutManager(this)
    resultsList.adapter = adapter
    adapter.onItemClick = ::onMovieClick
  }

  private fun onMovieClick(id: String) {
    presenter.movieClicked(id)
  }

  override val searchStream: Observable<String>
    get() = RxTextView.afterTextChangeEvents(searchEdit)
      .map { it.editable().toString() }.doOnNext {
        enableClear(it.isNotEmpty())
      }.filter { it.isNotEmpty() }

  override val clearStream: Observable<Any>
    get() = RxView.clicks(clearSearch)

  override fun showMovies(movies: List<Movie>) {
    adapter.addMovies(movies)
    empty.visibility(false)
    resultsList.visibility(true)
  }

  override fun showEmpty() {
    empty.visibility(true)
    resultsList.visibility(false)
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
