package com.xently.xui.adapters.table

import android.view.Gravity
import android.widget.LinearLayout
import com.evrencoskun.tableview.TableView
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder
import com.xently.xui.databinding.XuiDataTableCellBinding
import com.xently.xui.models.Cell
import com.xently.xui.utils.getThemedColor

/**
 * Used to populate [TableView]
 */
class CellViewHolder(
    private val binding: XuiDataTableCellBinding,
    private val alignValuesCenter: Set<Int>,
    private val alignValuesRight: Set<Int>,
    private val readOnly: Boolean = false
) : AbstractViewHolder(binding.root) {

    fun setTextViewData(model: Cell, columnPosition: Int) {
        val data = model.data
        val dataStr = data.toString()
        /*val dataIsNumber =
            (dataStr.toIntOrNull() ?: dataStr.toFloatOrNull() ?: dataStr.toFloatOrNull()) != null*/
        with(binding.cell) {
            text = dataStr
            gravity = if (data is Number || alignValuesRight.contains(columnPosition)) {
                Gravity.END or Gravity.CENTER_VERTICAL
            } else if (alignValuesCenter.contains(columnPosition)) Gravity.CENTER
            else Gravity.START or Gravity.CENTER_VERTICAL
        }

        // It is REQUIRED to remeasure itself
        binding.container.layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
        binding.cell.requestLayout()
    }

    override fun setSelected(selectionState: SelectionState) {
        if (readOnly) return
        super.setSelected(selectionState)

        with(binding.cell) {
            setTextColor(context.getThemedColor(android.R.attr.textColorPrimary))
        }
    }
}

/*
fun TextView.changeTextColorOnSelectionStateChange(state: SelectionState) {
    cell.setTextColor(
        ContextCompat.getColor(
            context, when (state) {
                SELECTED -> R.color.selected_text_color
                UNSELECTED -> R.color.unselected_text_color
                SHADOWED -> R.color.unselected_text_color
            }
        )
    )
}*/
