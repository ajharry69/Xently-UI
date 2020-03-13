package com.xently.xui.utils.ui.fragment

import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.xently.dialog.ButtonText
import com.xently.dialog.DialogParams
import com.xently.dialog.MessageDialog
import com.xently.dialog.XentlyDialog
import com.xently.xui.R
import com.xently.xui.utils.Log
import com.xently.xui.utils.ui.IModifyToolbar
import com.xently.xui.utils.ui.view.IView

interface IFragment : IView {

    /**
     * What to set as title of [Toolbar]
     * @see IModifyToolbar
     */
    val toolbarTitle: String? get() = null

    /**
     * Option to hide/show [Toolbar]. Default behaviour is **true** (show)
     * @see IModifyToolbar
     */
    val showToolbar: Boolean get() = true

    /**
     * Option to hide/show [Toolbar]'s **Up-Icon/Arrow** for back-navigation. Default behaviour is
     * [true] (show)
     * @see IModifyToolbar
     */
    val showToolbarUpIcon: Boolean get() = true

    val toolbarUpIcon: Int?
        @DrawableRes get() = null

    /**
     * What to do when system/phone's **_back-button_** is pressed
     */
    fun onBackPressed() {
        Log.show("BaseFragment", "onBackPressed: ${this::class.java.name}")
    }

    fun Fragment.hideKeyboard() = hideKeyboard(requireContext(), view)

    fun Fragment.onCreateDeletionDialog(message: String): MessageDialog {
        return MessageDialog.getInstance(
            DialogParams(
                getString(R.string.xui_delete_dialog_title),
                message, ButtonText(
                    getString(R.string.xui_delete_dialog_pos_btn),
                    getString(R.string.xui_delete_dialog_neg_btn)
                )
            ),
            icon = android.R.drawable.stat_sys_warning
        ).apply {
            onDialogButtonClickListener = object : XentlyDialog.OnDialogButtonClickListener {
                override fun onDialogPositiveButtonClick(
                    dialog: DialogInterface,
                    index: Int,
                    tag: String?,
                    dialogXently: XentlyDialog
                ) {
                    onDeletionDialogDeleteClick()
                    dialog.dismiss()
                }
            }
        }
    }

    fun Fragment.onCreateDeletionDialog(@StringRes message: Int): MessageDialog =
        onCreateDeletionDialog(getString(message))

    fun onDeletionDialogDeleteClick() = Unit

    /**
     * @param permission permission requested for. Input should be from Manifest permission constants
     * **`Manifest.permission.CAMERA`** or **`Manifest.permission_group.STORAGE`**
     * @param requestCode it helps in properly responding to specific permissions as dictated by
     * [onRequestPermissionsResult]
     * @param onPermissionGranted (Execute on Permission [permission] Granted/Available) - what to
     * do when the [permission] is granted by the user
     * @param onRationaleNeeded what to do when the [permission] request justification is needed for
     * the user to understand reason for permission request. Default behaviour is to show an alert
     * dialog explaining why the permission is required
     */
    fun <T> Fragment.requestFeaturePermission(
        permission: String,
        requestCode: Int,
        onPermissionGranted: (() -> T)? = null,
        onRationaleNeeded: (() -> Unit)? = null
    ) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return

        // Check if Camera permission is already granted or available
        if (ContextCompat.checkSelfPermission(
                this.requireContext(),
                permission
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Camera permission is already available. Show the preview
            onPermissionGranted?.invoke()
        } else {
            // Permission not available
            // Provide additional rationale to the user if the permission was not granted and the
            // user will benefit from the additional activity of the use of the permission

            if (shouldShowRequestPermissionRationale(permission)) {
                // Inform user of why you need the permission
                onRationaleNeeded?.invoke()
                // Proceed to request permission again
            }

            // Request permission
            requestPermissions(arrayOf(permission), requestCode)
        }
    }
}