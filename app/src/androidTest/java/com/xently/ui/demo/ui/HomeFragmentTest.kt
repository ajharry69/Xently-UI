package com.xently.ui.demo.ui

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.xently.ui.demo.R
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
@MediumTest
class HomeFragmentTest {

    private lateinit var navController: NavController
    private lateinit var scenario: FragmentScenario<HomeFragment>

    @Before
    fun setUp() {
        navController = mock(NavController::class.java)
        scenario = launchFragmentInContainer(
            themeResId = R.style.AppTheme,
            factory = HomeFragmentFactory()
        )
        with(scenario) {
            onFragment {
                Navigation.setViewNavController(it.requireView(), navController)
            }
        }
    }

    @Test
    fun clickButtonsToNavigate() {
        navigateToCoreUIFragmentOnCoreUIButtonClick()

        navigateToDialogUIFragmentOnDialogUIButtonClick()
    }

    private fun navigateToCoreUIFragmentOnCoreUIButtonClick() {
        onView(withId(R.id.core_ui)).check(ViewAssertions.matches(withText(R.string.test_core_ui)))
            .perform(click())

        scenario.onFragment {
            verify(navController).navigate(HomeFragmentDirections.coreUi())
        }
    }

    private fun navigateToDialogUIFragmentOnDialogUIButtonClick() {
        onView(withId(R.id.dialog_ui)).check(ViewAssertions.matches(withText(R.string.test_dialog_ui)))
            .perform(click())

        scenario.onFragment {
            verify(navController).navigate(HomeFragmentDirections.dialogUi())
        }
    }

}