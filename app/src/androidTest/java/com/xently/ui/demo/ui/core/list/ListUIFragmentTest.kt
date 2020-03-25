package com.xently.ui.demo.ui.core.list

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.xently.ui.demo.R
import com.xently.ui.demo.RecyclerViewItemCount
import com.xently.ui.demo.adapters.EmployeeListAdapter.EmployeeViewHolder
import com.xently.ui.demo.clickDialogButton
import com.xently.ui.demo.clickSnackbarActionButton
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock

@RunWith(AndroidJUnit4::class)
@MediumTest
class ListUIFragmentTest {

    private lateinit var navController: NavController
    private lateinit var scenario: FragmentScenario<ListUIFragment>

    @Before
    fun setUp() {
        navController = mock(NavController::class.java)
        scenario = launchFragmentInContainer<ListUIFragment>(
            fragmentArgs = ListUIFragmentArgs().toBundle(),
            themeResId = R.style.AppTheme,
            factory = ListUIFragmentFactory()
        )
        with(scenario) {
            onFragment {
                Navigation.setViewNavController(it.requireView(), navController)
            }
        }
    }

    @Test
    fun clickListItemInvokesListItemClickListener() {
        onView(withId(R.id.list)).run {
            perform(actionOnItemAtPosition<EmployeeViewHolder>(0, click()))

//        checkSnackBarDisplayedWithMessage("FName") // Cannot check specific name
            clickSnackbarActionButton(R.string.fire) // Shows Dialog
            clickDialogButton(R.string.delete) // Fire's employee...

            check(RecyclerViewItemCount.decrementedBy(1))
        }
    }

    @Test
    fun clickAddFabToAddEmployee() {
        onView(withId(R.id.fab)).perform(click())

        onView(withId(R.id.list)).check(RecyclerViewItemCount.incrementedBy(1))
    }
}