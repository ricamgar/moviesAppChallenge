package de.mytoysgroup.movies.challenge.moviesearch

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import de.mytoysgroup.movies.challenge.R
import de.mytoysgroup.movies.challenge.common.domain.model.Movie
import de.mytoysgroup.movies.challenge.common.util.load
import kotlinx.android.synthetic.main.item_movie.view.*

class SearchMovieAdapter : RecyclerView.Adapter<SearchMovieAdapter.ViewHolder>() {

  private val movies = mutableListOf<Movie>()
  var onItemClick: ((String) -> Unit)? = null

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view = LayoutInflater.from(parent.context)
      .inflate(R.layout.item_movie, parent, false)
    return ViewHolder(view, onItemClick)
  }

  override fun getItemCount(): Int = movies.size

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.bind(movies[position])
  }

  fun addMovies(movies: List<Movie>) {
    this.movies.addAll(movies)
    notifyDataSetChanged()
  }

  fun clear() {
    this.movies.clear()
    notifyDataSetChanged()
  }

  class ViewHolder(
    view: View,
    private val onItemClick: ((String) -> Unit)?
  ) : RecyclerView.ViewHolder(view) {
    fun bind(movie: Movie) {
      with(movie) {
        itemView.cell.setOnClickListener { onItemClick?.invoke(movie.id) }
        itemView.poster.load(poster, R.drawable.ic_no_poster)
        itemView.title.text = title
        itemView.year.text = year
      }
    }
  }
}