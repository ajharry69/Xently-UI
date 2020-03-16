package com.xently.ui.demo.ui.core

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
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
class CoreUIFragmentTest {

    private lateinit var navController: NavController
    private lateinit var scenario: FragmentScenario<CoreUIFragment>

    @Before
    fun setUp() {
        navController = mock(NavController::class.java)
        scenario = launchFragmentInContainer(
            themeResId = R.style.AppTheme,
            factory = CoreUIFragmentFactory()
        )
        with(scenario) {
            onFragment {
                Navigation.setViewNavController(it.requireView(), navController)
            }
        }
    }

    @Test
    fun buttonClicks() {
        navigateToListUIFragmentOnListUIButtonClick()

        navigateToTableUIFragmentOnTableUIButtonClick()
    }

    private fun navigateToListUIFragmentOnListUIButtonClick() {
        onView(withId(R.id.core_list_ui)).check(matches(withText(R.string.test_core_list_ui)))
            .perform(ViewActions.click())

        scenario.onFragment {
            verify(navController).navigate(CoreUIFragmentDirections.coreListUi())
        }
    }

    private fun navigateToTableUIFragmentOnTableUIButtonClick() {
        onView(withId(R.id.core_table_ui)).check(matches(withText(R.string.test_core_table_ui)))
            .perform(ViewActions.click())

        scenario.onFragment {
            verify(navController).navigate(CoreUIFragmentDirections.coreTableUi())
        }
    }

}