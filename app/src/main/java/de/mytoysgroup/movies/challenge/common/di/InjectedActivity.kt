package de.mytoysgroup.movies.challenge.common.di

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.util.Log
import de.mytoysgroup.movies.challenge.MoviesApp
import de.mytoysgroup.movies.challenge.common.presenter.BasePresenter
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.android.retainedKodein

abstract class InjectedActivity : AppCompatActivity(), KodeinAware, BasePresenter.View {

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

  override fun showError(error: Throwable) {
    Log.e("ERROR", error.message)
  }

}

fun Activity.app() = applicationContext as MoviesApp