package com.xently.xui.utils.ui

import android.view.View
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar


/**
 * Shows [Snackbar] for given [duration] with an optional action button labeled [actionButtonText]
 * that does [actionButtonClick] when clicked
 * @param actionButtonClick: Callback for responding to [Snackbar] action button click
 * @param actionButtonText: Label text shown on [Snackbar]s action button
 * @see Snackbar.make
 */
@JvmOverloads
fun showSnackBar(
    view: View,
    message: String,
    duration: Int = Snackbar.LENGTH_SHORT,
    actionButtonText: String? = null,
    actionButtonClick: ((snackBar: Snackbar) -> Unit)? = null
): Snackbar {
    val snackbar = Snackbar.make(view, message, duration)
    with(snackbar) {
//        setActionTextColor(ContextCompat.getColor(context, R.color.secondaryLightColor))
        if (actionButtonText != null) setAction(actionButtonText) {
            actionButtonClick?.invoke(this)
        }
        if (!isShownOrQueued) show()
    }

    return snackbar
}

/**
 * @see showSnackBar
 */
@JvmOverloads
fun showSnackBar(
    view: View,
    @StringRes message: Int,
    duration: Int = Snackbar.LENGTH_SHORT,
    actionButtonText: String? = null,
    actionButtonClick: ((snackBar: Snackbar) -> Unit)? = null
): Snackbar {
    val snackbar = Snackbar.make(view, message, duration)
    with(snackbar) {
        if (actionButtonText != null) setAction(actionButtonText) {
            actionButtonClick?.invoke(this)
        }
        if (!isShownOrQueued) show()
    }

    return snackbar
}