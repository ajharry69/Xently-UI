package com.xently.xui.adapters.table

import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.evrencoskun.tableview.TableView
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder
import com.xently.xui.R
import com.xently.xui.models.Cell
import com.xently.xui.utils.getThemedColor

/**
 * Used to populate [TableView]
 */
class CellViewHolder(
    view: View,
    private val alignValuesCenter: Set<Int>,
    private val alignValuesRight: Set<Int>
) : AbstractViewHolder(view) {

    private val cell: TextView = itemView.findViewById(R.id.cell)
    private val container: LinearLayout = itemView.findViewById(R.id.container)

    fun setTextViewData(model: Cell, columnPosition: Int) {
        val data = model.data
        val dataStr = data.toString()
        /*val dataIsNumber =
            (dataStr.toIntOrNull() ?: dataStr.toFloatOrNull() ?: dataStr.toFloatOrNull()) != null*/
        with(cell) {
            text = dataStr
            gravity = if (data is Number || alignValuesRight.contains(columnPosition)) {
                Gravity.END or Gravity.CENTER_VERTICAL
            } else if (alignValuesCenter.contains(columnPosition)) Gravity.CENTER
            else Gravity.START or Gravity.CENTER_VERTICAL
        }

        // It is REQUIRED to remeasure itself
        container.layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
        cell.requestLayout()
    }

    override fun setSelected(selectionState: SelectionState) {
        super.setSelected(selectionState)
        val context = cell.context

        cell.setTextColor(getThemedColor(context, android.R.attr.textColorPrimary))
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
