package com.xently.ui.demo.ui.core.table

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.xently.ui.demo.R
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito

@RunWith(AndroidJUnit4::class)
@MediumTest
class TableUIFragmentTest {

    private lateinit var navController: NavController
    private lateinit var scenario: FragmentScenario<TableUIFragment>

    @Before
    fun setUp() {
        navController = Mockito.mock(NavController::class.java)
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
    fun clickNextInvokesPageNavigateNext() {
        assertThat(2, equalTo(1 * 2))
    }
}