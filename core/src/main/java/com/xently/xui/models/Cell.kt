package com.xently.xui.models

import com.evrencoskun.tableview.TableView
import com.evrencoskun.tableview.sort.ISortableModel

/**
 * Model/Data class containing DataTable's ([TableView]) cell data
 */
data class Cell(val dataId: String, val data: Any?) : ISortableModel {

    override fun getContent(): Any? = data

    override fun getId(): String = dataId
}