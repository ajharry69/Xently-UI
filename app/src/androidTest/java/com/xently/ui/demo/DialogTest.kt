package com.xently.ui.demo

import androidx.fragment.app.testing.launchFragment
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.xently.xui.dialog.DatePickerDialog
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@MediumTest
@Ignore("Requires a change in architecture")
class DialogTest {

    @Test
    fun showDatePicker() {
        val scenario = launchFragment<DatePickerDialog>(themeResId = R.style.AppTheme)
        with(scenario) {
            onFragment { fragment ->
                assertThat(fragment.dialog, notNullValue())
                assertThat(fragment.requireDialog().isShowing, `is`(true))
                fragment.dismiss()
                fragment.parentFragmentManager.executePendingTransactions()
                assertThat(fragment.dialog, nullValue())
            }
        }
    }

}