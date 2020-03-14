package com.xently.ui.demo

import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.pressBack
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.rule.ActivityTestRule
import org.hamcrest.Matchers.*
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@MediumTest
class MainActivityTest {
    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun showMessageDialogOne() {
        onView(withText(R.string.show_message_dialog_one)).perform(click())
        onView(withText(android.R.string.ok)).inRoot(isDialog())
            .check(matches(isDisplayed())).perform(click())
        pressBack()
    }

    @Test
    fun showMessageDialogTwo() {
        onView(withText(R.string.show_message_dialog_two)).perform(click())
        onView(withText(R.string.okay)).inRoot(isDialog()).check(matches(isDisplayed()))
            .perform(click())
        pressBack()
    }

    @Test
    fun showCustomLayoutDialog() {
        onView(withText(R.string.show_custom_layout_dialog)).perform(click())
        pressBack()
    }

    @Test
    fun showFullScreenDialog() {
        onView(withText(R.string.show_fullscreen_dialog)).perform(click())
        pressBack()
    }

    @Test
    fun showMultiChoiceDialog() {
        with(onView(withText(R.string.show_multi_choice_dialog))) {
            assertNotNull(this)
            perform(click())

            val selectable = MainActivity.choiceDialogData.slice(0..2 step 2)

            selectable.forEach {
                onData(allOf(`is`(instanceOf(String::class.java)), `is`(it))).perform(click())
            }

            onView(withText(R.string.positive_button)).inRoot(isDialog())
                .check(matches(isDisplayed())).perform(click())

            checkSnackBarDisplayedByMessage("Selected: $selectable")
        }
        pressBack()
    }

    @Test
    fun showSingleChoiceDialog() {
        onView(withText(R.string.show_single_choice_dialog)).perform(click())

        onData(`is`(instanceOf(String::class.java))).atPosition(2).perform(click())
        pressBack()
    }

    @Test
    fun showListChoiceDialog() {
        onView(withText(R.string.show_list_choice_dialog)).perform(click())
        pressBack()
    }

    @Test
    fun showTimePickerDialog() {
        onView(withText(R.string.show_time_picker)).perform(click())
        pressBack()
    }

    @Test
    fun showDatePickerDialog() {
        onView(withText(R.string.show_date_picker)).perform(click())
        pressBack()
    }
}