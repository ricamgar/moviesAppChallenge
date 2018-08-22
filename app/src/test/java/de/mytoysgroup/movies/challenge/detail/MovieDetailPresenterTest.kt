package de.mytoysgroup.movies.challenge.detail

import android.content.Context
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import de.mytoysgroup.movies.challenge.common.di.appModule
import de.mytoysgroup.movies.challenge.common.domain.model.Movie
import de.mytoysgroup.movies.challenge.common.domain.repository.MoviesLocalRepository
import de.mytoysgroup.movies.challenge.common.domain.repository.MoviesRemoteRepository
import io.reactivex.Completable
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
class MovieDetailPresenterTest : KodeinAware {

  private val presenter: MovieDetailPresenter by instance()
  private val id = "ANY_ID"

  @Mock
  lateinit var context: Context
  @Mock
  lateinit var moviesRemoteRepository: MoviesRemoteRepository
  @Mock
  lateinit var moviesLocalRepository: MoviesLocalRepository
  @Mock
  lateinit var view: MovieDetailPresenter.View

  override val kodein = Kodein.lazy {
    import(appModule(context))
    import(movieDetailModule(id))

    bind<MoviesRemoteRepository>(overrides = true) with singleton { moviesRemoteRepository }
    bind<MoviesLocalRepository>(overrides = true) with singleton { moviesLocalRepository }
    bind<Scheduler>(tag = "bg", overrides = true) with singleton { Schedulers.trampoline() }
    bind<Scheduler>(tag = "main", overrides = true) with singleton { Schedulers.trampoline() }
  }

  @Test
  fun showMovieDetailNotFavorite() {
    whenever(moviesRemoteRepository.get(id)).thenReturn(Single.just(anyMovie))
    whenever(moviesLocalRepository.getAll()).thenReturn(Single.just(listOf(anotherMovie)))

    presenter.resume(view)

    verify(view).showDetails(anyMovie, false)
  }

  @Test
  fun showMovieDetailFavorite() {
    whenever(moviesRemoteRepository.get(id)).thenReturn(Single.just(anyMovie))
    whenever(moviesLocalRepository.getAll()).thenReturn(Single.just(listOf(anyMovie)))

    presenter.resume(view)

    verify(view).showDetails(anyMovie, true)
  }

  @Test
  fun saveMovieAndShowAsFavorite() {
    whenever(moviesRemoteRepository.get(id)).thenReturn(Single.just(anyMovie))
    whenever(moviesLocalRepository.getAll()).thenReturn(Single.just(listOf(anotherMovie)))
    whenever(moviesLocalRepository.save(any())).thenReturn(Completable.complete())

    presenter.resume(view)
    presenter.addClicked(anyMovie)

    verify(view).showDetails(anyMovie, false)
    verify(view).showDetails(anyMovie, true)
  }

  @Test
  fun removeMovieAndShowAsNotFavorite() {
    whenever(moviesRemoteRepository.get(id)).thenReturn(Single.just(anyMovie))
    whenever(moviesLocalRepository.getAll()).thenReturn(Single.just(listOf(anyMovie)))
    whenever(moviesLocalRepository.remove(any())).thenReturn(Completable.complete())

    presenter.resume(view)
    presenter.removeClicked(anyMovie)

    verify(view).showDetails(anyMovie, true)
    verify(view).showDetails(anyMovie, false)
  }

  companion object {
    val anyMovie = Movie(
      title = "Title",
      year = "2018",
      id = "123",
      poster = "http://abc"
    )

    val anotherMovie = Movie(
      title = "Title 2",
      year = "2018",
      id = "456",
      poster = "http://def"
    )
  }
}