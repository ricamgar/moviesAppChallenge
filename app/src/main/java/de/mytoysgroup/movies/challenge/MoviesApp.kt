package de.mytoysgroup.movies.challenge

import android.app.Application
import android.support.annotation.VisibleForTesting
import de.mytoysgroup.movies.challenge.common.di.appModule
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware

class MoviesApp : Application(), KodeinAware {

  @VisibleForTesting
  var overrideBindings: Kodein.MainBuilder.() -> Unit = {}

  override var kodein = Kodein.lazy {
    import(appModule(applicationContext))
  }
}
