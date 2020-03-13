package com.xently.xui.adapters.table

import androidx.core.content.ContextCompat
import com.evrencoskun.tableview.TableView
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder.SelectionState.*
import com.xently.xui.R
import com.xently.xui.databinding.DataTableRowHeaderBinding
import com.xently.xui.utils.ui.view.table.RowHeader

/**
 * Used to populate [TableView]
 */
class RowHeaderViewHolder(private val binding: DataTableRowHeaderBinding) :
    AbstractViewHolder(binding.root) {

    fun setTextViewData(model: RowHeader) {
        binding.data.text = model.data
    }

    override fun setSelected(selectionState: SelectionState?) {
        super.setSelected(selectionState)
        if (selectionState == null) return

        val color = when (selectionState) {
            SELECTED -> R.color.selected_text_color
            UNSELECTED -> R.color.unselected_text_color
            SHADOWED -> R.color.unselected_text_color
        }

        binding.data.setTextColor(ContextCompat.getColor(binding.data.context, color))
    }
}