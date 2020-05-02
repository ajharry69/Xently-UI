package com.xently.xui.adapters.table

import android.view.Gravity
import android.widget.LinearLayout
import com.evrencoskun.tableview.ITableView
import com.evrencoskun.tableview.TableView
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractSorterViewHolder
import com.evrencoskun.tableview.sort.SortState
import com.evrencoskun.tableview.sort.SortState.ASCENDING
import com.evrencoskun.tableview.sort.SortState.DESCENDING
import com.xently.xui.R
import com.xently.xui.databinding.XuiDataTableColumnHeaderBinding
import com.xently.xui.models.ColumnHeader
import com.xently.xui.utils.getThemedColor
import com.xently.xui.utils.ui.view.hideViews
import com.xently.xui.utils.ui.view.showViews
import java.util.*

/**
 * Used to populate [TableView]
 */
class ColumnHeaderViewHolder(
    private val binding: XuiDataTableColumnHeaderBinding,
    private val iTableView: ITableView,
    private val readOnly: Boolean = false
) : AbstractSorterViewHolder(binding.root) {

    val title: String? get() = binding.data.text.toString().toUpperCase(Locale.getDefault())

    fun setData(model: ColumnHeader) {
        with(binding.data) {
            text = model.data
            gravity = Gravity.START or Gravity.CENTER_VERTICAL
        }

        // It is REQUIRED to remeasure itself
        resetColumnDimensions()
    }

    fun onSortRequested() {
        if (!readOnly) {
            binding.sort.setOnClickListener {
                when (sortState) {
                    ASCENDING -> iTableView.sortColumn(adapterPosition, ASCENDING)
                    DESCENDING -> iTableView.sortColumn(adapterPosition, DESCENDING)
                    else -> iTableView.sortColumn(adapterPosition, DESCENDING)
                }
            }
        }
    }

    fun hideSortIcon() {
        hideViews(binding.sort)
    }

    fun getViewHolderAtPosition(position: Int): ColumnHeaderViewHolder? =
        iTableView.columnHeaderRecyclerView.findViewHolderForAdapterPosition(position) as ColumnHeaderViewHolder?

    override fun setSelected(selectionState: SelectionState) {
        if (readOnly) return
        super.setSelected(selectionState)

        with(binding.data) {
            setTextColor(context.getThemedColor(android.R.attr.textColorPrimary))
        }
    }

    override fun onSortingStatusChanged(pSortState: SortState) {
        if (readOnly) return
        super.onSortingStatusChanged(pSortState)

        // It is necessary to remeasure itself.
        resetColumnDimensions()

        controlSortState(pSortState)

        binding.sort.requestLayout()
        itemView.requestLayout()
    }

    private fun controlSortState(state: SortState?) {
        when (state) {
            ASCENDING -> {
                with(binding.sort) {
                    showViews(this)
                    setImageResource(R.drawable.xui_ic_action_arrow_up)
                }
            }
            DESCENDING -> {
                with(binding.sort) {
                    showViews(this)
                    setImageResource(R.drawable.xui_ic_action_arrow_down)
                }
            }
            else -> hideViews(binding.sort)
        }
    }

    private fun resetColumnDimensions() {
        with(binding) {
            with(container) {
                layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
                requestLayout()
            }
            data.requestLayout()
        }
    }
}