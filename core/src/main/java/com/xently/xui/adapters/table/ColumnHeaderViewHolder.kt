package com.xently.xui.adapters.table

import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.evrencoskun.tableview.ITableView
import com.evrencoskun.tableview.TableView
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractSorterViewHolder
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder.SelectionState.*
import com.evrencoskun.tableview.sort.SortState
import com.evrencoskun.tableview.sort.SortState.ASCENDING
import com.evrencoskun.tableview.sort.SortState.DESCENDING
import com.xently.xui.R
import com.xently.xui.databinding.DataTableColumnHeaderBinding
import com.xently.xui.utils.ui.view.table.ColumnHeader
import java.util.*

/**
 * Used to populate [TableView]
 */
class ColumnHeaderViewHolder(
    private val binding: DataTableColumnHeaderBinding,
    private val iTableView: ITableView
) : AbstractSorterViewHolder(binding.root) {

    val title: String? get() = binding.data.text.toString().toUpperCase(Locale.getDefault())

    fun setData(model: ColumnHeader) {
        with(binding.data) {
            text = model.data
            gravity = Gravity.START or Gravity.CENTER_VERTICAL
        }

        // It is REQUIRED to remeasure itself
        binding.container.layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
        binding.data.requestLayout()
    }

    fun onSortClick() {
        binding.sort.setOnClickListener {
            when (sortState) {
                ASCENDING -> iTableView.sortColumn(adapterPosition, ASCENDING)
                DESCENDING -> iTableView.sortColumn(adapterPosition, DESCENDING)
                else -> iTableView.sortColumn(adapterPosition, DESCENDING)
            }
        }
    }

    fun hideSort() {
        binding.sort.visibility = View.GONE
    }

    fun getViewHolderAtPosition(position: Int): ColumnHeaderViewHolder? =
        iTableView.columnHeaderRecyclerView.findViewHolderForAdapterPosition(position) as ColumnHeaderViewHolder?

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

    override fun onSortingStatusChanged(pSortState: SortState?) {
        super.onSortingStatusChanged(pSortState)

        // It is necessary to remeasure itself.
        binding.container.layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT

        controlSortState(pSortState)

        binding.data.requestLayout()
        binding.sort.requestLayout()
        binding.container.requestLayout()
        itemView.requestLayout()
    }

    private fun controlSortState(state: SortState?) {
        when (state) {
            ASCENDING -> {
                with(binding.sort) {
                    visibility = View.VISIBLE
                    setImageResource(R.drawable.ic_action_arrow_up)
                }
            }
            DESCENDING -> {
                with(binding.sort) {
                    visibility = View.VISIBLE
                    setImageResource(R.drawable.ic_action_arrow_down)
                }
            }
            else -> binding.sort.visibility = View.GONE
        }
    }
}