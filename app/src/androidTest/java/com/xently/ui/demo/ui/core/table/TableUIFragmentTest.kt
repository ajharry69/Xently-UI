package com.xently.ui.demo.ui.core.table

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.evrencoskun.tableview.pagination.Pagination
import com.xently.ui.demo.R
import org.hamcrest.Matchers.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
@MediumTest
class TableUIFragmentTest {

    private lateinit var navController: NavController
    private lateinit var pagination: Pagination
    private lateinit var scenario: FragmentScenario<TableUIFragment>

    @Before
    fun setUp() {
        navController = mock(NavController::class.java)
        pagination = mock(Pagination::class.java)
        scenario = launchFragmentInContainer<TableUIFragment>(
            themeResId = R.style.AppTheme,
            factory = TableUIFragmentFactory()
        )
        with(scenario) {
            onFragment {
                Navigation.setViewNavController(it.requireView(), navController)
            }
        }
    }

    @Test
    fun clickNextInvokesPageTurnNext() {
        scenario.onFragment {
            // Mock pagination
            it.pagination = pagination
        }
        onView(withContentDescription(R.string.xui_data_table_button_next)).perform(click())
        // Check if page turn next method was called at most 1x
        scenario.onFragment {
            verify(pagination, Mockito.never()).previousPage()
            verify(pagination, Mockito.atLeastOnce()).nextPage()
        }
    }

    @Test
    fun clickPreviousInvokesPageTurnPrevious() {
        scenario.onFragment {
            // Mock pagination
            it.pagination = pagination
        }
        onView(withContentDescription(R.string.xui_data_table_button_previous)).perform(click())
        // Check if page turn previous method was called at most 1x
        scenario.onFragment {
            verify(pagination, Mockito.atLeastOnce()).previousPage()
            verify(pagination, Mockito.never()).nextPage()
        }
    }

    @Test
    fun submitGotoPageInputUsingIMEACTIONToGoToPageInput() {
        scenario.onFragment {
            // Mock pagination
            it.pagination = pagination
        }
        val page = 2
        onView(withId(R.id.page)).perform(typeText("$page"), pressImeActionButton())
            .check(matches(withText("$page")))

        scenario.onFragment {
            verify(pagination, Mockito.atLeastOnce()).goToPage(page)
        }
    }

    @Test
    fun submitGotoPageInputUsingSUBMITBUTTONToGoToPageInput() {
        scenario.onFragment {
            // Mock pagination
            it.pagination = pagination
        }
        val page = 2
        onView(withId(R.id.page)).perform(typeText(page.toString()))
        onView(withId(R.id.submit_page)).perform(click())

        scenario.onFragment {
            verify(pagination, Mockito.atLeastOnce()).goToPage(page)
        }
    }

    @Test
    fun selectSpinnerItemToChangePageSizeEntries() {
        val pageCount = "20"
        with(onView(withId(R.id.page_size))) {
            perform(click())
            onData(allOf(`is`(instanceOf(String::class.java)), `is`(pageCount)))
                .perform(click())
            check(matches(withSpinnerText(pageCount)))
        }
        // Check if footer text is changed to a text containing selected page size
        onView(withId(R.id.footer)).check(matches(withText(containsString(pageCount))))
    }
}