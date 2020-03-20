package com.xently.xui.adapters.table

import android.view.Gravity
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import com.evrencoskun.tableview.ITableView
import com.evrencoskun.tableview.TableView
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractSorterViewHolder
import com.evrencoskun.tableview.sort.SortState
import com.evrencoskun.tableview.sort.SortState.ASCENDING
import com.evrencoskun.tableview.sort.SortState.DESCENDING
import com.xently.xui.R
import com.xently.xui.models.ColumnHeader
import com.xently.xui.utils.getThemedColor
import java.util.*

/**
 * Used to populate [TableView]
 */
class ColumnHeaderViewHolder(
    view: View,
    private val iTableView: ITableView
) : AbstractSorterViewHolder(view) {

    private val data = itemView.findViewById<TextView>(R.id.data)
    private val container: LinearLayout = itemView.findViewById(R.id.container)
    private val sort: ImageButton = itemView.findViewById(R.id.sort)

    val title: String? get() = data.text.toString().toUpperCase(Locale.getDefault())

    fun setData(model: ColumnHeader) {
        with(data) {
            text = model.data
            gravity = Gravity.START or Gravity.CENTER_VERTICAL
        }

        // It is REQUIRED to remeasure itself
        resetColumnDimensions()
    }

    fun onSortRequested() {
        sort.setOnClickListener {
            when (sortState) {
                ASCENDING -> iTableView.sortColumn(adapterPosition, ASCENDING)
                DESCENDING -> iTableView.sortColumn(adapterPosition, DESCENDING)
                else -> iTableView.sortColumn(adapterPosition, DESCENDING)
            }
        }
    }

    fun hideSortIcon() {
        sort.visibility = View.GONE
    }

    fun getViewHolderAtPosition(position: Int): ColumnHeaderViewHolder? =
        iTableView.columnHeaderRecyclerView.findViewHolderForAdapterPosition(position) as ColumnHeaderViewHolder?

    override fun setSelected(selectionState: SelectionState) {
        super.setSelected(selectionState)

        val data = data
        val context = data.context

        data.setTextColor(getThemedColor(context, android.R.attr.textColorPrimary))
    }

    override fun onSortingStatusChanged(pSortState: SortState) {
        super.onSortingStatusChanged(pSortState)

        // It is necessary to remeasure itself.
        resetColumnDimensions()

        controlSortState(pSortState)

        sort.requestLayout()
        itemView.requestLayout()
    }

    private fun controlSortState(state: SortState?) {
        when (state) {
            ASCENDING -> {
                with(sort) {
                    visibility = View.VISIBLE
                    setImageResource(R.drawable.ic_action_arrow_up)
                }
            }
            DESCENDING -> {
                with(sort) {
                    visibility = View.VISIBLE
                    setImageResource(R.drawable.ic_action_arrow_down)
                }
            }
            else -> sort.visibility = View.GONE
        }
    }

    private fun resetColumnDimensions() {
        with(container) {
            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
            requestLayout()
        }
        data.requestLayout()
    }
}