package co.cdmunoz.magic8ballgame

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class Magic8BallGameActivityEspressoTest {

  @get:Rule public val activityRule: ActivityTestRule<Magic8BallGameActivity> = ActivityTestRule<Magic8BallGameActivity>(
      Magic8BallGameActivity::class.java)

  @Test fun ensureTextChangesWork() {
    onView(withId(R.id.shake)).perform(click())
    onView(withId(R.id.message)).check(matches(not(withText(""))))
  }
}