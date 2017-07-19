package org.imozerov.babylonapp

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import org.imozerov.babylonapp.ui.posts.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import android.content.pm.ActivityInfo
import android.app.Activity
import android.R.attr.orientation
import android.content.res.Configuration
import android.support.test.InstrumentationRegistry



// TODO Remove this test. It will be flacky as it uses real data. Never ever run it on CI.
// The reason to have right not is to automate quick checks if app works.
// Mock everything starting from ViewModels, add custom TestRunner to avoid
// unneeded Dagger injection.
@LargeTest
@RunWith(AndroidJUnit4::class)
class SanityCheck {
    @JvmField
    @Rule
    var activityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun check() {
        val recyclerView = onView(withId(R.id.posts_list))
        rotateScreen()
        recyclerView.perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        rotateScreen()
        val textView = onView(withId(R.id.details_title))
        textView.check(matches(isDisplayed()))
    }

    private fun rotateScreen() {
        val context = InstrumentationRegistry.getTargetContext()
        val orientation = context.resources.configuration.orientation

        val activity = activityRule.activity
        activity.requestedOrientation = if (orientation == Configuration.ORIENTATION_PORTRAIT)
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        else
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }
}
