package de.mytoysgroup.movies.challenge.wishlist

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import de.mytoysgroup.movies.challenge.R
import de.mytoysgroup.movies.challenge.common.di.InjectedActivity
import de.mytoysgroup.movies.challenge.common.domain.model.Movie
import de.mytoysgroup.movies.challenge.common.util.visibility
import kotlinx.android.synthetic.main.activity_wishlist.*
import org.kodein.di.Kodein
import org.kodein.di.generic.instance

class WishlistActivity : InjectedActivity(), WishlistPresenter.View {

  private val presenter: WishlistPresenter by instance()
  private val adapter = WishlistAdapter()

  override fun activityModule() = Kodein.Module {
    import(wishlistModule())
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_wishlist)
    setSupportActionBar(toolbar)
    setupMoviesWishlist()

    fab.setOnClickListener { presenter.onSearchClicked() }
  }

  override fun onResume() {
    super.onResume()
    presenter.resume(this)
  }

  override fun onPause() {
    super.onPause()
    presenter.pause()
  }

  private fun setupMoviesWishlist() {
    moviesList.setHasFixedSize(true)
    moviesList.layoutManager = LinearLayoutManager(this)
    moviesList.adapter = adapter
    adapter.onItemClick = ::onItemClick
  }

  private fun onItemClick(id: String) {

  }

  override fun showEmpty() {
    empty.visibility(true)
    moviesList.visibility(false)
  }

  override fun showMovies(movies: List<Movie>) {
    adapter.addMovies(movies)
    empty.visibility(false)
    moviesList.visibility(true)
  }

  override fun setLoadingVisivility(visible: Boolean) {
    loading.visibility(visible)
  }

  override fun showError(error: Throwable) {
    Snackbar.make(fab, "Error: ${error.message}", Snackbar.LENGTH_LONG).show()
  }
}
