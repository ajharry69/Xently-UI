package com.xently.xui.adapters.table

import android.view.View
import android.widget.TextView
import com.evrencoskun.tableview.TableView
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder
import com.xently.xui.R
import com.xently.xui.models.RowHeader
import com.xently.xui.utils.getThemedColor

/**
 * Used to populate [TableView]
 */
class RowHeaderViewHolder(view: View) : AbstractViewHolder(view) {

    private val data: TextView = itemView.findViewById(R.id.data)

    fun setTextViewData(model: RowHeader) {
        data.text = model.data
    }

    override fun setSelected(selectionState: SelectionState) {
        super.setSelected(selectionState)

        data.setTextColor(getThemedColor(data.context, android.R.attr.textColorPrimary))
    }
}