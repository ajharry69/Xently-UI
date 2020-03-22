package com.xently.xui.utils.ui

import androidx.annotation.DrawableRes
import androidx.appcompat.widget.Toolbar

/**
 * Listens and responds to changes on [Toolbar] properties e.g.:
 *  1. **Title**
 *  2. **Up-Icon/Arrow** *visibility*
 *  3. **Toolbar** *visibility*
 * @see onModifyToolbar
 */
interface IModifyToolbar {

    /**
     * @param title use as [Toolbar] title
     * @param show if **true** [Toolbar] is shown
     */
    fun onModifyToolbar(
        title: String?,
        subTitle: String?,
        @DrawableRes upIcon: Int? = null,
        show: Boolean = true
    )
}