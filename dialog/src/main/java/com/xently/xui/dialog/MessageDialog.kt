package com.xently.xui.dialog

import android.app.Dialog
import androidx.annotation.DrawableRes
import androidx.annotation.StyleRes
import com.xently.dialog.DialogParams

/**
 * @see DialogFragment
 */
class MessageDialog : DialogFragment() {

    companion object {

        /**
         * @param icon what to use as [Dialog]'s **icon**
         * @param theme What to use as the alert [Dialog]'s theme. If `null`, default app theme is
         * applied
         * @see DialogFragment
         */
        fun getInstance(
            params: DialogParams,
            @DrawableRes
            icon: Int? = null,
            @StyleRes theme: Int? = null
        ) = MessageDialog().apply {
            this.dialogIconFromResource = icon
            this.dialogThemeFromResource = theme
            this.dialogTitle = params.title
            this.dialogMessage = params.message
            params.buttonText?.let { this.dialogButtonText = it }
        }
    }
}