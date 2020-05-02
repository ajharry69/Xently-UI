package com.xently.xui.models

import android.os.Parcel
import android.os.Parcelable
import com.evrencoskun.tableview.sort.SortState

/**
 * @param alignValuesCenter A set of column positions whose values should be aligned to the
 * **CENTER** of the cell. Default alignment is to the **START** or **LEFT** of the cell. **0**
 * is the position of the first column in the data-table
 * @param alignValuesRight A set of column positions whose values should be aligned to the
 * **RIGHT** of the cell. Default alignment is to the **START** or **LEFT** of the cell. **0**
 * is the position of the  first column in the data-table
 * @param hideColumnAtPosition Do not show column at position provided. **0** is the position of
 * the first column in the data-table
 * @param defaultSortColumnPosition Setup/initialize data table sorted in [defaultSortOrder] order
 * using values at column index provided. **0** is the position of the first column in the
 * data-table
 * @param defaultSortOrder Sort order to use when sorting table column at [defaultSortColumnPosition]
 */
data class TableConfig(
    val readOnly: Boolean = false,
    val alignValuesCenter: Set<Int> = emptySet(),
    val alignValuesRight: Set<Int> = emptySet(),
    val hideColumnAtPosition: Int? = null,
    val defaultSortColumnPosition: Int? = null,
    val defaultSortOrder: SortState = SortState.ASCENDING
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readByte() != 0.toByte(),
        parcel.createIntArray()?.toSet() ?: emptySet(),
        parcel.createIntArray()?.toSet() ?: emptySet(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        SortState.valueOf(parcel.readString()!!)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.run {
            writeByte(if (readOnly) 1 else 0)
            writeIntArray(alignValuesCenter.toIntArray())
            writeIntArray(alignValuesRight.toIntArray())
            writeValue(hideColumnAtPosition)
            writeValue(defaultSortColumnPosition)
            writeString(defaultSortOrder.name)
        }
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<TableConfig> {
        override fun createFromParcel(parcel: Parcel) = TableConfig(parcel)

        override fun newArray(size: Int): Array<TableConfig?> = arrayOfNulls(size)
    }
}