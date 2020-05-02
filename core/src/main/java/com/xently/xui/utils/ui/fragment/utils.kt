package com.xently.xui.utils.ui.fragment

import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.xently.xui.utils.ui.view.hideKeyboard

fun Fragment.hideKeyboard() = hideKeyboard(view)

fun com.xently.xui.Fragment.setupToolbar(toolbar: Toolbar) {
    toolbar.apply {
        setNavigationOnClickListener {
            onBackPressed()
        }
        setOnMenuItemClickListener {
            onOptionsItemSelected(it)
        }
    }
}