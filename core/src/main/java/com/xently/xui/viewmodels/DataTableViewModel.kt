package com.xently.xui.viewmodels

import android.content.Context
import com.xently.xui.utils.ui.view.table.Cell
import com.xently.xui.utils.ui.view.table.ColumnHeader
import com.xently.xui.utils.ui.view.table.RowHeader

abstract class DataTableViewModel<T> {
    /**
     * Used to populate table rows. A single(1) list of [Cell]s equals one(1) row of data in a table.
     *
     * **N/B:** Number of cells per row should equal number of columns returned by [columnHeaderData]
     */
    abstract fun rowCellsData(context: Context, list: List<T>): List<List<Cell>>

    /**
     * Used to name(title) table columns
     */
    abstract fun columnHeaderData(context: Context): List<ColumnHeader>

    /**
     * Used to name(title) rows
     */
    open fun rowHeaderData(context: Context, list: List<T>): List<RowHeader> =
        list.mapIndexed { i, _ -> RowHeader("${i + 1}") }
}