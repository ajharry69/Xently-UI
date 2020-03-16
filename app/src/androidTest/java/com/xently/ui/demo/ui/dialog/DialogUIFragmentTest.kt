package com.xently.ui.demo.ui.dialog

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.filters.MediumTest
import com.xently.ui.demo.R
import com.xently.ui.demo.checkSnackBarDisplayedByMessage
import org.hamcrest.Matchers
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock

@RunWith(AndroidJUnit4::class)
@MediumTest
class DialogUIFragmentTest {

    private lateinit var navController: NavController
    private lateinit var scenario: FragmentScenario<DialogUIFragment>

    @Before
    fun setUp() {
        navController = mock(NavController::class.java)
        scenario = launchFragmentInContainer<DialogUIFragment>(
            themeResId = R.style.AppTheme,
            factory = DialogUIFragmentFactory()
        )
        with(scenario) {
            onFragment {
                Navigation.setViewNavController(it.requireView(), navController)
            }
        }
    }

    @Test
    fun clickButtons() {
        showMessageDialogOne()

        showMessageDialogTwo()

        showCustomLayoutDialog()

        showMultiChoiceDialog()

        showSingleChoiceDialog()

        showListChoiceDialog()

        showTimePickerDialog()

        showDatePickerDialog()

        ViewActions.pressBack()
    }

    private fun showMessageDialogOne() {
        onView(withText(R.string.show_message_dialog_one))
            .perform(click())

        onView(withText(android.R.string.ok)).inRoot(isDialog())
            .check(matches(isDisplayed())).perform(click())
    }


    private fun showMessageDialogTwo() {
        onView(withText(R.string.show_message_dialog_two))
            .perform(click())
        onView(withText(R.string.okay))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))
            .perform(click())
    }


    private fun showCustomLayoutDialog() {
        onView(withText(R.string.show_custom_layout_dialog))
            .perform(click())

        onView(withText(R.string.positive_button))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))
            .perform(click())
    }

    private fun showMultiChoiceDialog() {
        with(onView(withText(R.string.show_multi_choice_dialog))) {
            assertNotNull(this)
            perform(click())

            val selectable = DialogUIFragment.choiceDialogData.slice(0..2 step 2)

            selectable.forEach {
                onData(allOf(`is`(Matchers.instanceOf(String::class.java)), `is`(it)))
                    .perform(click())
            }

            onView(withText(R.string.positive_button))
                .inRoot(isDialog())
                .check(matches(isDisplayed())).perform(click())

            checkSnackBarDisplayedByMessage("Selected: $selectable")
        }
    }

    private fun showSingleChoiceDialog() {
        onView(withText(R.string.show_single_choice_dialog)).perform(click())

        onData(`is`(Matchers.instanceOf(String::class.java)))
            .atPosition(2).perform(click())
    }

    private fun showListChoiceDialog() {
        onView(withText(R.string.show_list_choice_dialog)).perform(click())

        onData(`is`(Matchers.instanceOf(String::class.java)))
            .atPosition(2).perform(click())
    }

    private fun showTimePickerDialog() {
        onView(withText(R.string.show_time_picker)).perform(click())

        onView(withText(R.string.custom_picker_positive_button))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))
            .perform(click())
    }

    private fun showDatePickerDialog() {
        onView(withText(R.string.show_date_picker)).perform(click())
        onView(withText(android.R.string.ok)).inRoot(isDialog())
            .check(matches(isDisplayed())).perform(click())
    }
}