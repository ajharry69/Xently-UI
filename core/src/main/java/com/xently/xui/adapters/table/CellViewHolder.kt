package com.xently.xui.adapters.table

import android.view.Gravity
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.evrencoskun.tableview.TableView
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder.SelectionState.*
import com.xently.xui.R
import com.xently.xui.databinding.DataTableCellBinding
import com.xently.xui.utils.ui.view.table.Cell

/**
 * Used to populate [TableView]
 */
class CellViewHolder(
    private val binding: DataTableCellBinding,
    private val alignValuesCenter: Set<Int>,
    private val alignValuesRight: Set<Int>
) : AbstractViewHolder(binding.root) {

    fun setTextViewData(model: Cell, columnPosition: Int) {
        val data = model.data
        with(binding.cell) {
            text = data.toString()
            gravity = if (data is Number || alignValuesRight.contains(columnPosition)) {
                Gravity.END or Gravity.CENTER_VERTICAL
            } else if (alignValuesCenter.contains(columnPosition)) {
                Gravity.CENTER
            } else {
                Gravity.START or Gravity.CENTER_VERTICAL
            }
        }

        // It is REQUIRED to remeasure itself
        binding.container.layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
        binding.cell.requestLayout()
    }

    override fun setSelected(selectionState: SelectionState?) {
        super.setSelected(selectionState)
        if (selectionState == null) return

        val color = when (selectionState) {
            SELECTED -> R.color.selected_text_color
            UNSELECTED -> R.color.unselected_text_color
            SHADOWED -> R.color.unselected_text_color
        }

        binding.cell.setTextColor(ContextCompat.getColor(binding.cell.context, color))
    }
}