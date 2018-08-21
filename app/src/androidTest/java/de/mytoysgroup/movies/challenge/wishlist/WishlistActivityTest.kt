package de.mytoysgroup.movies.challenge.wishlist

import android.content.Intent
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withText
import com.nhaarman.mockito_kotlin.whenever
import de.mytoysgroup.movies.challenge.R
import de.mytoysgroup.movies.challenge.common.domain.model.Movie
import de.mytoysgroup.movies.challenge.util.OverridesRule
import de.mytoysgroup.movies.challenge.util.recyclerview.RecyclerViewInteraction
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
class WishlistActivityTest {

  private val context = InstrumentationRegistry.getTargetContext()

  @Mock
  private lateinit var getWishlist: GetWishlist

  @Rule
  @JvmField
  var activityRule: IntentsTestRule<WishlistActivity> =
    IntentsTestRule(WishlistActivity::class.java, true, false)

  @Rule
  @JvmField
  val overridesRule: OverridesRule = OverridesRule {
    bind<Scheduler>(tag = "bg", overrides = true) with singleton { Schedulers.trampoline() }
    bind<Scheduler>(tag = "main", overrides = true) with singleton { Schedulers.trampoline() }
    bind<GetWishlist>(overrides = true) with provider { getWishlist }
  }

  @Test
  fun showWishlistMoviesWhenNotEmpty() {
    whenever(getWishlist.execute()).thenReturn(Single.just(movies))

    activityRule.launchActivity(Intent())

    RecyclerViewInteraction.onRecyclerView<Movie>(ViewMatchers.withId(R.id.moviesList))
      .withItems(movies)
      .check { movie, view, e ->
        ViewAssertions.matches(ViewMatchers.hasDescendant(ViewMatchers.withText("Title ${movie.id}"))).check(view, e)
      }
  }

  @Test
  fun showEmptyViewWhenNoWishlist() {
    whenever(getWishlist.execute()).thenReturn(Single.just(emptyList()))

    activityRule.launchActivity(Intent())

    onView(withText(context.getString(R.string.no_wishlist))).check(matches(isDisplayed()))
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