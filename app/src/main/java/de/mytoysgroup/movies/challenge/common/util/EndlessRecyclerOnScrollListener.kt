package de.mytoysgroup.movies.challenge.common.util

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

abstract class EndlessRecyclerOnScrollListener(
  private val mLinearLayoutManager: LinearLayoutManager,
  private val visibleThreshold: Int
) : RecyclerView.OnScrollListener() {

  private var previousTotal = 0
  private var isLoading = true
  private var currentPage = 1

  fun reset(previousTotal: Int, loading: Boolean) {
    this.previousTotal = previousTotal
    this.isLoading = loading
    this.currentPage = 1
  }

  override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
    super.onScrolled(recyclerView, dx, dy)

    val visibleItemCount = recyclerView!!.childCount
    val totalItemCount = mLinearLayoutManager.itemCount
    val firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition()

    if (isLoading) {
      if (totalItemCount > previousTotal) {
        isLoading = false
        previousTotal = totalItemCount
      }
    }
    if (!isLoading && totalItemCount - visibleItemCount <= firstVisibleItem + visibleThreshold) {
      currentPage++

      onLoadMore(currentPage)

      isLoading = true
    }
  }

  abstract fun onLoadMore(currentPage: Int)
}