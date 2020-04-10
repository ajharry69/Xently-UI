package com.xently.xui.utils.ui.fragment

import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment


/**
 * @param permission permission requested for. Input should be from Manifest permission constants
 * **`Manifest.permission.CAMERA`** or **`Manifest.permission_group.STORAGE`**
 * @param requestCode it helps in properly responding to specific permissions as dictated by
 * [Fragment.onRequestPermissionsResult]
 * @param onPermissionGranted (Execute on Permission [permission] Granted/Available) - what to
 * do when the [permission] is granted by the user
 * @param onRationaleNeeded what to do when the [permission] request justification is needed for
 * the user to understand reason for permission request. Default behaviour is to show an alert
 * dialog explaining why the permission is required
 */
@JvmOverloads
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