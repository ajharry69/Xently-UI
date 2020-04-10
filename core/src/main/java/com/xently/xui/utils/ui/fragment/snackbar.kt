package com.xently.xui.utils.ui.fragment

import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.xently.xui.utils.ui.showSnackBar

@JvmOverloads
fun Fragment.showSnackBar(
    message: String,
    duration: Int = Snackbar.LENGTH_SHORT, actionButtonText: String? = null,
    actionButtonClick: ((snackBar: Snackbar) -> Unit)? = null
) = showSnackBar(requireView(), message, duration, actionButtonText, actionButtonClick)

@JvmOverloads
fun Fragment.showSnackBar(
    @StringRes message: Int,
    duration: Int = Snackbar.LENGTH_SHORT, actionButtonText: String? = null,
    actionButtonClick: ((snackBar: Snackbar) -> Unit)? = null
) = showSnackBar(requireView(), message, duration, actionButtonText, actionButtonClick)