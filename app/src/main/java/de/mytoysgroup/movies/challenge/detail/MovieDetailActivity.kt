package de.mytoysgroup.movies.challenge.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.view.MenuItem
import de.mytoysgroup.movies.challenge.R
import de.mytoysgroup.movies.challenge.common.di.InjectedActivity
import de.mytoysgroup.movies.challenge.common.domain.model.Movie
import de.mytoysgroup.movies.challenge.common.util.load
import de.mytoysgroup.movies.challenge.common.util.visibility
import kotlinx.android.synthetic.main.activity_movie_detail.*
import org.kodein.di.Kodein
import org.kodein.di.generic.instance

class MovieDetailActivity : InjectedActivity(), MovieDetailPresenter.View {

  private val presenter: MovieDetailPresenter by instance()

  override fun activityModule() = Kodein.Module {
    import(movieDetailModule(intent.getStringExtra("ID_EXTRA")))
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_movie_detail)
    setSupportActionBar(toolbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    fab.setOnClickListener { view ->
      Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
        .setAction("Action", null).show()
    }
  }

  override fun onResume() {
    super.onResume()
    presenter.attachView(this)
  }

  override fun onPause() {
    super.onPause()
    presenter.detachView()
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      android.R.id.home -> {
        onBackPressed()
        return true
      }
    }
    return super.onOptionsItemSelected(item)
  }

  override fun showDetails(movie: Movie, isFavorite: Boolean) {
    with(toolbarLayout) {
      title = movie.title
      setExpandedTitleColor(ContextCompat.getColor(this@MovieDetailActivity, android.R.color.transparent))
      setCollapsedTitleTextColor(ContextCompat.getColor(this@MovieDetailActivity, android.R.color.white))
    }

    with(movie) {
      movieTxt.text = title
      posterImg.load(poster, R.drawable.ic_no_poster)
      yearTxt.text = year
      durationTxt.text = runtime
      countryTxt.text = country
      plotTxt.text = plot
      actorsTxt.text = actors
      directorTxt.text = director
    }

    fab.isActivated = isFavorite
    fab.setOnClickListener {
      if (isFavorite) presenter.removeClicked(movie)
      else presenter.addClicked(movie)
    }
    detailView.visibility(true)
  }

  override fun setLoadingVisibility(visible: Boolean) {
    loading.visibility(visible)
  }

  companion object {
    fun getIntent(context: Context, id: String) =
      Intent(context, MovieDetailActivity::class.java)
        .apply { putExtra("ID_EXTRA", id) }
  }
}
