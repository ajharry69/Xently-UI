package com.xently.xui.dialog.utils

import androidx.annotation.RestrictTo

/**
 * Contains properties and methods that returns date formatting [String]s
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
object DateFormat {

    /**
     * Time in HH:mm
     */
    const val TIME_HM_24_HRS = "HH:mm"

    /**
     * Time in HH:mm aa
     */
    const val TIME_HM_12_HRS = "hh:mm aa"

    /**
     * Date in yyyy[ds]MM[ds]dd
     * @param ds Date Separator. Default (-)
     */
    fun dateOnly(ds: Char = '-'): String = "yyyy${ds}MM${ds}dd"
}