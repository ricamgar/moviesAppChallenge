package de.mytoysgroup.movies.challenge.common.di

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import de.mytoysgroup.movies.challenge.MoviesApp
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.android.retainedKodein

abstract class InjectedActivity : AppCompatActivity(), KodeinAware {

  private val appKodein by closestKodein()

  override val kodein: Kodein by retainedKodein {
    extend(appKodein)
    import(baseActivityModule(this@InjectedActivity), allowOverride = true)
    import(activityModule())
    (app().overrideBindings)()
  }

  /**
   * Override if specific DI are needed
   */
  open fun activityModule() = Kodein.Module {

  }

}

fun Activity.app() = applicationContext as MoviesApp