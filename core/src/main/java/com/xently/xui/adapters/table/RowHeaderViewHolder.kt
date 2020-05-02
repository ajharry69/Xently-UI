package com.xently.xui.adapters.table

import com.evrencoskun.tableview.TableView
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder
import com.xently.xui.databinding.XuiDataTableRowHeaderBinding
import com.xently.xui.models.RowHeader
import com.xently.xui.utils.getThemedColor

/**
 * Used to populate [TableView]
 */
class RowHeaderViewHolder(
    private val binding: XuiDataTableRowHeaderBinding,
    private val readOnly: Boolean = false
) : AbstractViewHolder(binding.root) {

    fun setTextViewData(model: RowHeader) {
        binding.data.text = model.data
    }

    override fun setSelected(selectionState: SelectionState) {
        if (readOnly) return
        super.setSelected(selectionState)

        with(binding.data) {
            setTextColor(context.getThemedColor(android.R.attr.textColorPrimary))
        }
    }
}