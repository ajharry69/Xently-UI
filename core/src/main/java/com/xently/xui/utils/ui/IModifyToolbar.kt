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
     * @param hide if **true** [Toolbar] is hided
     * @param hideUpIcon if **true**, **Up-Icon/Arrow** is NOT shown otherwise it's shown
     */
    fun onModifyToolbar(
        title: String?,
        hide: Boolean,
        hideUpIcon: Boolean = hide,
        @DrawableRes upIcon: Int? = null
    )
}