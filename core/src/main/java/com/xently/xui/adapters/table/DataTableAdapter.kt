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
import com.xently.xui.databinding.XuiDataTableCellBinding
import com.xently.xui.databinding.XuiDataTableColumnHeaderBinding
import com.xently.xui.databinding.XuiDataTableRowHeaderBinding
import com.xently.xui.models.Cell
import com.xently.xui.models.ColumnHeader
import com.xently.xui.models.RowHeader
import com.xently.xui.models.TableConfig
import com.xently.xui.viewmodels.DataTableViewModel

/**
 * Used to populate [TableView]
 */
class DataTableAdapter<T>(
    private val context: Context,
    private val viewModel: DataTableViewModel<T>,
    private val tableConfig: TableConfig = TableConfig()
) : AbstractTableAdapter<ColumnHeader, RowHeader, Cell>() {

    fun submitList(list: Iterable<T>) {
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
        XuiDataTableColumnHeaderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ), tableView, tableConfig.readOnly
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
            XuiDataTableRowHeaderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            tableConfig.readOnly
        )

    override fun onBindRowHeaderViewHolder(
        holder: AbstractViewHolder,
        rowHeaderItemModel: RowHeader?,
        rowPosition: Int
    ) {
        val model: RowHeader = rowHeaderItemModel ?: return
        (holder as? RowHeaderViewHolder)?.setTextViewData(model)
    }

    override fun onCreateCellViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder =
        CellViewHolder(
            XuiDataTableCellBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            tableConfig.alignValuesCenter, tableConfig.alignValuesRight, tableConfig.readOnly
        )

    override fun onBindCellViewHolder(
        holder: AbstractViewHolder,
        cellItemModel: Cell?,
        columnPosition: Int,
        rowPosition: Int
    ) {
        val model: Cell = cellItemModel ?: return
        (holder as? CellViewHolder)?.setTextViewData(model, columnPosition)
    }

    @SuppressLint("InflateParams")
    override fun onCreateCornerView(parent: ViewGroup): View = LayoutInflater.from(context)
        .inflate(R.layout.xui_data_table_corner, null, false)

    override fun getCellItemViewType(position: Int): Int = 0

    override fun getColumnHeaderItemViewType(position: Int): Int = 0

    override fun getRowHeaderItemViewType(position: Int): Int = 0
}