package com.xently.xui.adapters.table

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.evrencoskun.tableview.TableView
import com.evrencoskun.tableview.adapter.AbstractTableAdapter
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder
import com.xently.xui.R
import com.xently.xui.databinding.DataTableCellBinding
import com.xently.xui.databinding.DataTableColumnHeaderBinding
import com.xently.xui.databinding.DataTableRowHeaderBinding
import com.xently.xui.utils.ui.view.table.Cell
import com.xently.xui.utils.ui.view.table.ColumnHeader
import com.xently.xui.utils.ui.view.table.RowHeader
import com.xently.xui.viewmodels.DataTableViewModel

/**
 * Used to populate [TableView]
 */
class DataTableAdapter<T>(
    private val context: Context,
    private val viewModel: DataTableViewModel<T>,
    private val alignValuesCenter: Set<Int> = emptySet(),
    private val alignValuesRight: Set<Int> = emptySet()
) : AbstractTableAdapter<ColumnHeader, RowHeader, Cell>(context) {

    fun <L : List<T>> submitList(list: L) {
        setAllItems(
            viewModel.columnHeaderData(),
            viewModel.rowHeaderData(list),
            viewModel.submitCellData(list)
        )
    }

    override fun onCreateColumnHeaderViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AbstractViewHolder = ColumnHeaderViewHolder(
        DataTableColumnHeaderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ), tableView
    )

    override fun onBindColumnHeaderViewHolder(
        holder: AbstractViewHolder?,
        columnHeaderItemModel: Any?,
        columnPosition: Int
    ) {
        (holder as ColumnHeaderViewHolder?)?.apply {
            (columnHeaderItemModel as ColumnHeader?)?.let { setData(it) }
            onSortClick()
        }
    }

    override fun onCreateRowHeaderViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder =
        RowHeaderViewHolder(
            DataTableRowHeaderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindRowHeaderViewHolder(
        holder: AbstractViewHolder?,
        rowHeaderItemModel: Any?,
        rowPosition: Int
    ) {
        val rh: RowHeaderViewHolder = holder as RowHeaderViewHolder? ?: return
        val model: RowHeader = rowHeaderItemModel as RowHeader? ?: return

        rh.setTextViewData(model)
    }

    override fun onCreateCellViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder =
        CellViewHolder(
            DataTableCellBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            alignValuesCenter, alignValuesRight
        )

    override fun onBindCellViewHolder(
        holder: AbstractViewHolder?,
        cellItemModel: Any?,
        columnPosition: Int,
        rowPosition: Int
    ) {
        val ch: CellViewHolder = holder as CellViewHolder? ?: return
        val model: Cell = cellItemModel as Cell? ?: return

        ch.setTextViewData(model, columnPosition)
    }

    @SuppressLint("InflateParams")
    override fun onCreateCornerView(): View = LayoutInflater.from(context)
        .inflate(R.layout.data_table_corner, null, false)

    override fun getCellItemViewType(position: Int): Int = 0

    override fun getColumnHeaderItemViewType(position: Int): Int = 0

    override fun getRowHeaderItemViewType(position: Int): Int = 0
}