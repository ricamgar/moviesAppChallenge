package de.mytoysgroup.movies.challenge.common.data.local

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import de.mytoysgroup.movies.challenge.common.di.appModule
import de.mytoysgroup.movies.challenge.common.domain.model.Movie
import de.mytoysgroup.movies.challenge.common.domain.repository.MoviesLocalRepository
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

@RunWith(AndroidJUnit4::class)
class MoviesLocalRepositoryTest : KodeinAware {

  private val localRepository: MoviesLocalRepository by instance()

  override val kodein = Kodein.lazy {
    val context = InstrumentationRegistry.getTargetContext()
    import(appModule(context))
  }

  @Before
  fun setUp() {
    localRepository.removeAll().blockingAwait()
  }

  @After
  fun tearDown() {
    localRepository.removeAll().blockingAwait()
  }

  @Test
  fun shouldSaveAndGetMovieToFavorites() {
    localRepository.save(anyMovie)
      .test()
      .assertNoErrors()
      .assertComplete()

    localRepository.getAll()
      .test()
      .assertNoErrors()
      .assertValue(listOf(anyMovie))
  }

  @Test
  fun shouldReturnEmptyListWhenNoFavorites() {
    localRepository.getAll()
      .test()
      .assertNoErrors()
      .assertValue(listOf())
  }

  @Test
  fun shouldRemoveAMovieFromFavorites() {
    localRepository.save(anyMovie).blockingAwait()
    localRepository.save(anotherMovie).blockingAwait()

    localRepository.getAll()
      .test()
      .assertNoErrors()
      .assertValue(listOf(anyMovie, anotherMovie))

    localRepository.remove(anyMovie).blockingAwait()

    localRepository.getAll()
      .test()
      .assertNoErrors()
      .assertValue(listOf(anotherMovie))
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