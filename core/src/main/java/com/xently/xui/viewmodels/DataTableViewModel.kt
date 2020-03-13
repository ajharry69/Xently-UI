package com.xently.xui.viewmodels

import com.xently.xui.utils.ui.view.table.Cell
import com.xently.xui.utils.ui.view.table.ColumnHeader
import com.xently.xui.utils.ui.view.table.RowHeader

abstract class DataTableViewModel<T> {
    abstract fun submitCellData(list: List<T>): List<List<Cell>>

    abstract fun columnHeaderData(): List<ColumnHeader>

    fun rowHeaderData(list: List<T>): List<RowHeader> {
        val rd: MutableList<RowHeader> = ArrayList()

        list.forEachIndexed { i, _ ->
            rd.add(RowHeader("${i + 1}"))
        }

        return rd.toList()
    }
}