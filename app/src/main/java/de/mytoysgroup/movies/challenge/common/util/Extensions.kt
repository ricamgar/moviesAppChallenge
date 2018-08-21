package de.mytoysgroup.movies.challenge.common.util

import android.support.annotation.DrawableRes
import android.view.View
import android.widget.ImageView
import com.squareup.picasso.Picasso

fun View.visibility(visible: Boolean) {
  visibility = if (visible) View.VISIBLE else View.GONE
}

fun ImageView.load(url: String, @DrawableRes placeHolderRes: Int = 0) {
  Picasso.get()
    .load(url)
    .also {
      if (placeHolderRes != 0) it.placeholder(placeHolderRes)
    }
    .into(this)
}