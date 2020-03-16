package com.xently.ui.demo.ui.core.list

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.xently.ui.demo.R
import com.xently.ui.demo.adapters.EmployeeListAdapter.EmployeeViewHolder
import com.xently.ui.demo.data.Employee
import com.xently.xui.adapters.list.OnListItemClickListener
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock

@RunWith(AndroidJUnit4::class)
@MediumTest
class ListUIFragmentTest {

    private lateinit var listItemClickListener: OnListItemClickListener<Employee>
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
                listItemClickListener = it
                Navigation.setViewNavController(it.requireView(), navController)
            }
        }
    }

    @Test
    fun clickListItemInvokesListItemClickListener() {
        onView(withId(R.id.list))
            .perform(RecyclerViewActions.actionOnItemAtPosition<EmployeeViewHolder>(0, click()))

        assertThat(2, equalTo(1 + 1))
    }
}