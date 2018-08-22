package de.mytoysgroup.movies.challenge.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.MenuItem
import de.mytoysgroup.movies.challenge.R
import de.mytoysgroup.movies.challenge.common.di.InjectedActivity
import de.mytoysgroup.movies.challenge.common.domain.model.Movie
import de.mytoysgroup.movies.challenge.common.util.load
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
    presenter.resume(this)
  }

  override fun onPause() {
    super.onPause()
    presenter.pause()
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

  override fun showDetails(movie: Movie) {
    toolbarLayout.title = movie.title
    poster.load(movie.poster, R.drawable.ic_no_poster)
  }

  override fun setLoadingVisibility(visible: Boolean) {

  }

  companion object {
    fun getIntent(context: Context, id: String) =
      Intent(context, MovieDetailActivity::class.java)
        .apply { putExtra("ID_EXTRA", id) }
  }
}
