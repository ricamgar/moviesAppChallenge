package de.mytoysgroup.movies.challenge.common.navigator

import android.content.Context
import de.mytoysgroup.movies.challenge.moviesearch.SearchMovieActivity

class MovieAppNavigator(
  private val context: Context
) : Navigator {

  override fun goToSearch() {
    context.startActivity(SearchMovieActivity.getIntent(context))
  }
}