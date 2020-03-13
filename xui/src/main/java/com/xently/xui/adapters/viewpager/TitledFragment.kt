package com.xently.xui.adapters.viewpager

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment

data class TitledFragment(val fragment: Fragment, val title: CharSequence?) {
    constructor(context: Context, fragment: Fragment, @StringRes title: Int) : this(
        fragment,
        context.getString(title)
    )

    constructor(fragment: Fragment) : this(fragment, null)
}