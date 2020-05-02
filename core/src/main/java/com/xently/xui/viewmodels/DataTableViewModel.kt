package com.xently.xui.viewmodels

import android.content.Context
import com.xently.xui.models.Cell
import com.xently.xui.models.ColumnHeader
import com.xently.xui.models.RowHeader

abstract class DataTableViewModel<T> {
    /**
     * Used to populate table rows. A single(1) list of [Cell]s equals one(1) row of data in a table.
     *
     * **N/B:** Number of cells per row should equal number of columns returned by [columnHeaderData]
     */
    abstract fun rowCellsData(context: Context, list: Iterable<T>): List<List<Cell>>

    /**
     * Used to name(title) table columns
     */
    abstract fun columnHeaderData(context: Context): List<ColumnHeader>

    /**
     * Used to name(title) rows
     */
    open fun rowHeaderData(context: Context, list: Iterable<T>) = list.mapIndexed { i, _ ->
        RowHeader("${i + 1}")
    }
}