package com.xently.xui.utils.ui.view.table

import com.evrencoskun.tableview.TableView
import com.evrencoskun.tableview.sort.ISortableModel

/**
 * Model/Data class containing DataTable's ([TableView]) column or header data (title)
 */
data class ColumnHeader(val data: String)

/**
 * Model/Data class containing DataTable's ([TableView]) row data (title). Is usually
 * a numbering of [TableView] rows.
 */
data class RowHeader(val data: String)

/**
 * Model/Data class containing DataTable's ([TableView]) cell data
 */
data class Cell(val dataId: String, val data: Any?) : ISortableModel {

    override fun getContent(): Any? = data

    override fun getId(): String = dataId
}