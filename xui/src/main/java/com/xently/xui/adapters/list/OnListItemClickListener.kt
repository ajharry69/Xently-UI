package com.xently.xui.adapters.list

import android.view.View

interface OnListItemClickListener<M> {
    fun onListItemClick(model: M, view: View)
    fun onListItemLongClick(model: M, view: View): Boolean = false
}