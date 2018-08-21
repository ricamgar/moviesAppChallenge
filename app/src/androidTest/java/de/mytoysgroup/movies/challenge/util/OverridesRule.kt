package de.mytoysgroup.movies.challenge.util

import android.support.test.InstrumentationRegistry
import de.mytoysgroup.movies.challenge.MoviesApp
import org.junit.rules.ExternalResource
import org.kodein.di.Kodein

/**
 * Used to retrieve test application before every test and override dependencies.
 */
class OverridesRule(private val bindings: Kodein.MainBuilder.() -> Unit = {}) : ExternalResource() {

  private fun app(): MoviesApp =
    InstrumentationRegistry.getTargetContext().applicationContext as MoviesApp

  override fun before() {
    app().overrideBindings = bindings
  }

  override fun after() {
    app().overrideBindings = {}
  }
}
