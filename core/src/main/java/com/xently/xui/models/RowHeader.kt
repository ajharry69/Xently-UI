package com.xently.xui.models

import com.evrencoskun.tableview.TableView
import com.evrencoskun.tableview.sort.ISortableModel

/**
 * Model/Data class containing DataTable's ([TableView]) row data (title). Is usually
 * a numbering of [TableView] rows.
 */
data class RowHeader(val data: String) : ISortableModel {

    override fun getContent(): Any? = data

    override fun getId(): String = data
}