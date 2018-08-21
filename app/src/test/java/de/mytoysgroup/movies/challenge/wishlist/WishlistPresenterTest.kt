package de.mytoysgroup.movies.challenge.wishlist

import android.content.Context
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import de.mytoysgroup.movies.challenge.common.di.appModule
import de.mytoysgroup.movies.challenge.common.domain.model.Movie
import de.mytoysgroup.movies.challenge.common.domain.repository.MoviesLocalRepository
import de.mytoysgroup.movies.challenge.common.navigator.Navigator
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.Test
import org.junit.runner.RunWith
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class WishlistPresenterTest : KodeinAware {

  val presenter: WishlistPresenter by instance()

  @Mock
  lateinit var context: Context
  @Mock
  lateinit var localRepository: MoviesLocalRepository
  @Mock
  lateinit var navigator: Navigator
  @Mock
  lateinit var view: WishlistPresenter.View

  override val kodein = Kodein.lazy {
    import(appModule(context))
    import(wishlistModule())

    bind<MoviesLocalRepository>(overrides = true) with singleton { localRepository }
    bind<Navigator>(overrides = true) with singleton { navigator }
    bind<Scheduler>(tag = "bg", overrides = true) with singleton { Schedulers.trampoline() }
    bind<Scheduler>(tag = "main", overrides = true) with singleton { Schedulers.trampoline() }
  }

  @Test
  fun shouldShowWishlistMovies() {
    whenever(localRepository.getAll()).thenReturn(Single.just(movies))

    presenter.resume(view)

    verify(view).showMovies(movies)
  }

  @Test
  fun shouldShowEmptyWhenNoMovies() {
    whenever(localRepository.getAll()).thenReturn(Single.just(emptyList()))

    presenter.resume(view)

    verify(view).showEmpty()
  }

  @Test
  fun shouldOpenSearchWhenButtonClicked() {
    whenever(localRepository.getAll()).thenReturn(Single.just(emptyList()))

    presenter.resume(view)
    presenter.onSearchClicked()

    verify(navigator).goToSearch()
  }

  companion object {
    val movies = (1..5).map {
      Movie(
        title = "Title $it",
        year = "20$it",
        id = it.toString(),
        poster = "http://$it"
      )
    }
  }
}