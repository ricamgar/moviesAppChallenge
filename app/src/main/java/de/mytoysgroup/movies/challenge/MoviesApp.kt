package de.mytoysgroup.movies.challenge

import android.app.Application
import de.mytoysgroup.movies.challenge.common.di.appModule
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware

class MoviesApp : Application(), KodeinAware {

  override var kodein = Kodein.lazy {
    import(appModule(applicationContext))
  }
}
