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
import com.xently.xui.models.Cell
import com.xently.xui.models.ColumnHeader
import com.xently.xui.models.RowHeader
import com.xently.xui.viewmodels.DataTableViewModel

/**
 * Used to populate [TableView]
 */
class DataTableAdapter<T>(
    private val context: Context,
    private val viewModel: DataTableViewModel<T>,
    private val alignValuesCenter: Set<Int> = emptySet(),
    private val alignValuesRight: Set<Int> = emptySet()
) : AbstractTableAdapter<ColumnHeader, RowHeader, Cell>() {

    fun <L : List<T>> submitList(list: L) {
        setAllItems(
            viewModel.columnHeaderData(context),
            viewModel.rowHeaderData(context, list),
            viewModel.rowCellsData(context, list)
        )
    }

    override fun onCreateColumnHeaderViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AbstractViewHolder = ColumnHeaderViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.data_table_column_header,
            parent,
            false
        ), tableView
    )

    override fun onBindColumnHeaderViewHolder(
        holder: AbstractViewHolder,
        columnHeaderItemModel: ColumnHeader?,
        columnPosition: Int
    ) {
        (holder as ColumnHeaderViewHolder?)?.apply {
            columnHeaderItemModel?.let { setData(it) }
            onSortRequested()
        }
    }

    override fun onCreateRowHeaderViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder =
        RowHeaderViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.data_table_row_header,
                parent,
                false
            )
        )

    override fun onBindRowHeaderViewHolder(
        holder: AbstractViewHolder,
        rowHeaderItemModel: RowHeader?,
        rowPosition: Int
    ) {
        val rh: RowHeaderViewHolder = holder as RowHeaderViewHolder? ?: return
        val model: RowHeader = rowHeaderItemModel ?: return

        rh.setTextViewData(model)
    }

    override fun onCreateCellViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder =
        CellViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.data_table_cell,
                parent,
                false
            ),
            alignValuesCenter, alignValuesRight
        )

    override fun onBindCellViewHolder(
        holder: AbstractViewHolder,
        cellItemModel: Cell?,
        columnPosition: Int,
        rowPosition: Int
    ) {
        val ch: CellViewHolder = holder as CellViewHolder? ?: return
        val model: Cell = cellItemModel ?: return

        ch.setTextViewData(model, columnPosition)
    }

    @SuppressLint("InflateParams")
    override fun onCreateCornerView(parent: ViewGroup): View = LayoutInflater.from(context)
        .inflate(R.layout.data_table_corner, null, false)

    override fun getCellItemViewType(position: Int): Int = 0

    override fun getColumnHeaderItemViewType(position: Int): Int = 0

    override fun getRowHeaderItemViewType(position: Int): Int = 0
}