package de.mytoysgroup.movies.challenge.moviesearch

import android.content.Context
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import de.mytoysgroup.movies.challenge.common.di.appModule
import de.mytoysgroup.movies.challenge.common.domain.model.Movie
import de.mytoysgroup.movies.challenge.common.domain.repository.MoviesRemoteRepository
import io.reactivex.Observable
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
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SearchMoviePresenterTest : KodeinAware {

  private val presenter: SearchMoviePresenter by instance()

  @Mock
  lateinit var context: Context
  @Mock
  lateinit var moviesRemoteRepository: MoviesRemoteRepository
  @Mock
  lateinit var view: SearchMoviePresenter.View

  override val kodein = Kodein.lazy {
    import(appModule(context))
    import(searchMovieModule())

    bind<MoviesRemoteRepository>(overrides = true) with singleton { moviesRemoteRepository }
    bind<Scheduler>(tag = "bg", overrides = true) with singleton { Schedulers.trampoline() }
    bind<Scheduler>(tag = "main", overrides = true) with singleton { Schedulers.trampoline() }
  }

  @Test
  fun showMoviesOnSuccessfulSearch() {
    val searchTerm = "testTerm1"
    whenever(moviesRemoteRepository.search(searchTerm, 1))
      .thenReturn(Single.just(anyMoviesList(searchTerm)))
    whenever(view.searchStream).thenReturn(Observable.just(searchTerm))
    whenever(view.clearStream).thenReturn(Observable.empty())

    presenter.resume(view)

    verify(view).showMovies(anyMoviesList(searchTerm))
  }

  @Test
  fun showErrorOnFailedSearch() {
    val error = Exception()
    whenever(moviesRemoteRepository.search(anyString(), anyInt()))
      .thenReturn(Single.error(error))
    whenever(view.searchStream).thenReturn(Observable.just(""))
    whenever(view.clearStream).thenReturn(Observable.empty())

    presenter.resume(view)

    verify(view).showError(error)
  }

  private fun anyMoviesList(searchTerm: String): List<Movie> {
    return (1..10).map {
      Movie(
        title = "Title $searchTerm $it",
        year = "20$it",
        id = it.toString(),
        poster = "http://$it"
      )
    }
  }
}