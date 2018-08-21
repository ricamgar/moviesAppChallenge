package de.mytoysgroup.movies.challenge.common.data.remote

import android.content.Context
import de.mytoysgroup.movies.challenge.common.di.appModule
import de.mytoysgroup.movies.challenge.common.domain.repository.MoviesRemoteRepository
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Test
import org.junit.runner.RunWith
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File

@RunWith(MockitoJUnitRunner::class)
class MoviesRemoteRepositoryTest : KodeinAware {

  private val remoteRepository: MoviesRemoteRepository by instance()

  @Mock
  lateinit var context: Context
  private val mockWebServer = MockWebServer()

  override val kodein = Kodein.lazy {
    import(appModule(context))

    bind<OmdbApi>(overrides = true) with singleton {
      Retrofit.Builder()
        .baseUrl(mockWebServer.url(""))
        .addConverterFactory(MoshiConverterFactory.create(instance()))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(OmdbApi::class.java)
    }
  }

  @Test
  fun shouldFetchMoviesResults() {
    mockWebServer.enqueue(MockResponse().setBody(getJson("moviesResult.json")))

    remoteRepository.search("anyTerm")
      .test()
      .assertNoErrors()
      .assertValue { movies ->
        movies.size == 10 &&
          movies[0].id == "tt0097576" &&
          movies[9].id == "tt0275186"
      }
  }

  @Test
  fun shouldReturnEmptyListWhenNoResults() {
    mockWebServer.enqueue(MockResponse().setBody(getJson("noMovies.json")))

    remoteRepository.search("anyTerm")
      .test()
      .assertNoErrors()
      .assertValue(emptyList())
  }

  @Test
  fun shouldThrowErrorWhenResponseFailed() {
    mockWebServer.enqueue(MockResponse().setResponseCode(500))

    remoteRepository.search("anyTerm")
      .test()
      .assertError(Exception::class.java)
  }

  @Test
  fun shouldFetchASingleMovie() {
    mockWebServer.enqueue(MockResponse().setBody(getJson("movie.json")))

    remoteRepository.get("id")
      .test()
      .assertNoErrors()
      .assertValue { movie ->
        movie.id == "tt0097576" &&
          movie.title == "Indiana Jones and the Last Crusade" &&
          movie.year == "1989" &&
          movie.poster == "https://m.media-amazon.com/daasd.jpg" &&
          movie.actors == "Harrison Ford, Sean Connery, Denholm Elliott, Alison Doody" &&
          movie.director == "Steven Spielberg" &&
          movie.plot == "This is the plot"
      }
  }

  private fun getJson(path: String): String {
    val uri = this.javaClass.classLoader!!.getResource(path)
    val file = File(uri.path)
    return String(file.readBytes())
  }
}