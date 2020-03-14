package com.xently.ui.demo

import androidx.annotation.StringRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*


fun checkSnackBarDisplayedByMessage(@StringRes message: Int) {
    onView(withText(message))
        .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
}

fun checkSnackBarDisplayedByMessage(message: String) {
    onView(withText(message))
        .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
}