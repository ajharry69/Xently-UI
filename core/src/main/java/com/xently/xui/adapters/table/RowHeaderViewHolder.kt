package com.xently.xui.adapters.table

import com.evrencoskun.tableview.TableView
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder
import com.xently.xui.databinding.DataTableRowHeaderBinding
import com.xently.xui.utils.getThemedColor
import com.xently.xui.models.RowHeader

/**
 * Used to populate [TableView]
 */
class RowHeaderViewHolder(private val binding: DataTableRowHeaderBinding) :
    AbstractViewHolder(binding.root) {

    fun setTextViewData(model: RowHeader) {
        binding.data.text = model.data
    }

    override fun setSelected(selectionState: SelectionState) {
        super.setSelected(selectionState)

        val data = binding.data
        data.setTextColor(getThemedColor(data.context, android.R.attr.textColorPrimary))
    }
}