package com.xently.xui.utils.ui.fragment

import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.xently.xui.dialog.utils.ButtonText
import com.xently.xui.dialog.utils.DialogParams
import com.xently.xui.R
import com.xently.xui.dialog.MessageDialog

fun Fragment.onCreateDeletionDialog(message: String): MessageDialog {
    return MessageDialog.getInstance(
        DialogParams(
            getString(R.string.xui_delete_dialog_title),
            message, ButtonText(
                getString(R.string.xui_delete_dialog_pos_btn),
                getString(R.string.xui_delete_dialog_neg_btn)
            )
        ),
        icon = R.drawable.xui_ic_action_warning
    )
}

fun Fragment.onCreateDeletionDialog(
    @StringRes message: Int,
    vararg format: Any
): MessageDialog = onCreateDeletionDialog(getString(message, *format))