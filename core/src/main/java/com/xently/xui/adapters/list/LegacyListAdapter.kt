package com.xently.xui.adapters.list

import androidx.recyclerview.widget.RecyclerView

@Suppress("MemberVisibilityCanBePrivate")
abstract class LegacyListAdapter<M, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {

    private var list: Iterable<M> = emptyList()

    var listItemClickListener: OnListItemClickListener<M>? = null

    val currentList: Iterable<M>
        get() = this.list

    protected fun getItem(position: Int): M = currentList.elementAt(position)

    fun submitList(list: Iterable<M>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = currentList.count()

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