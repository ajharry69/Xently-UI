package com.xently.xui.adapters.list

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class ListAdapter<M, VH : RecyclerView.ViewHolder>(diffCallback: DiffUtil.ItemCallback<M>) :
    androidx.recyclerview.widget.ListAdapter<M, VH>(diffCallback) {

    var listItemClickListener: OnListItemClickListener<M>? = null

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position)
        with(holder.itemView) {
            setOnClickListener {
                listItemClickListener?.onListItemClick(item, it)
            }
            setOnLongClickListener {
                listItemClickListener?.onListItemLongClick(item, it) ?: false
            }
        }
    }
}

