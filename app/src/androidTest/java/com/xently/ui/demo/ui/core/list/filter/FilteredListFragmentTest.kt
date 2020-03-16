package com.xently.ui.demo.ui.core.list.filter

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.xently.ui.demo.R
import com.xently.ui.demo.adapters.EmployeeListAdapter.EmployeeViewHolder
import com.xently.ui.demo.data.Employee
import com.xently.xui.adapters.list.OnListItemClickListener
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito

@RunWith(AndroidJUnit4::class)
@MediumTest
class FilteredListFragmentTest {
    private lateinit var listItemClickListener: OnListItemClickListener<Employee>
    private lateinit var navController: NavController
    private lateinit var scenario: FragmentScenario<FilteredListFragment>

    @Before
    fun setUp() {
        navController = Mockito.mock(NavController::class.java)
        scenario = launchFragmentInContainer<FilteredListFragment>(
            fragmentArgs = FilteredListFragmentArgs("444").toBundle(),
            themeResId = R.style.AppTheme,
            factory = FilteredListFragmentFactory()
        )
        with(scenario) {
            onFragment {
                listItemClickListener = it
                Navigation.setViewNavController(it.requireView(), navController)
            }
        }
    }

    @Test
    fun clickListItemInvokesListItemClickListener() {
        onView(ViewMatchers.withId(R.id.list))
            .perform(RecyclerViewActions.actionOnItemAtPosition<EmployeeViewHolder>(0, click()))

        assertThat(2, Matchers.equalTo(1 + 1))
    }
}