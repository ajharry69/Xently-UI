package com.xently.xui.adapters.list

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class PagedListAdapter<M, VH : RecyclerView.ViewHolder>(diffCallback: DiffUtil.ItemCallback<M>) :
    androidx.paging.PagedListAdapter<M, VH>(diffCallback) {

    var listItemClickListener: OnListItemClickListener<M>? = null

    override fun onBindViewHolder(holder: VH, position: Int) = Unit
}