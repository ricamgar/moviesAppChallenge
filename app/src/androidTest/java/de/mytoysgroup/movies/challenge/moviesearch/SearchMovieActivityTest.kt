package de.mytoysgroup.movies.challenge.moviesearch

import android.content.Intent
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.typeText
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.espresso.matcher.ViewMatchers.*
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.whenever
import de.mytoysgroup.movies.challenge.R
import de.mytoysgroup.movies.challenge.common.domain.model.Movie
import de.mytoysgroup.movies.challenge.util.OverridesRule
import de.mytoysgroup.movies.challenge.util.recyclerview.RecyclerViewInteraction.onRecyclerView
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.kodein.di.generic.bind
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class SearchMovieActivityTest {

  @Mock
  private lateinit var searchMovie: SearchMovie

  @Rule
  @JvmField
  var activityRule: IntentsTestRule<SearchMovieActivity> =
    IntentsTestRule(SearchMovieActivity::class.java, true, false)

  @Rule
  @JvmField
  val overridesRule: OverridesRule = OverridesRule {
    bind<Scheduler>(tag = "bg", overrides = true) with singleton { Schedulers.trampoline() }
    bind<Scheduler>(tag = "main", overrides = true) with singleton { Schedulers.trampoline() }
    bind<Scheduler>(tag = "computation", overrides = true) with singleton { Schedulers.trampoline() }
    bind<SearchMovie>(overrides = true) with provider { searchMovie }
  }

  @Test
  fun showMoviesResultAfterSearch() {
    whenever(searchMovie.execute(any())).thenReturn(Single.just(movies))

    activityRule.launchActivity(Intent())
    onView(withId(R.id.searchEdit)).perform(typeText("a"))

    onRecyclerView<Movie>(withId(R.id.resultsList))
      .withItems(movies)
      .check { movie, view, e ->
        matches(hasDescendant(withText("Title ${movie.id}"))).check(view, e)
      }
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