package com.xently.xui.utils.ui.fragment

import android.view.View
import com.xently.xui.adapters.list.OnListItemClickListener

interface IListFragment<T> : OnListItemClickListener<T> {

    /**
     * Message to be shown when list of [T] records is empty
     */
    val noDataText: CharSequence?
        get() = null

    override fun onListItemClick(model: T, view: View) = Unit
}