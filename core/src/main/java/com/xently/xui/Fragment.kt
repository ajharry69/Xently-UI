package com.xently.xui

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.annotation.CallSuper
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import com.google.android.material.textfield.TextInputLayout
import com.xently.xui.dialog.DatePickerDialog
import com.xently.xui.dialog.DatePickerDialog.DateSetListener
import com.xently.xui.dialog.TimePickerDialog
import com.xently.xui.dialog.TimePickerDialog.TimeSetListener
import com.xently.xui.dialog.utils.ButtonText.CREATOR.fromStringResource
import com.xently.xui.utils.Icon
import com.xently.xui.utils.Icon.END
import com.xently.xui.utils.Icon.START
import com.xently.xui.utils.Log
import com.xently.xui.utils.ui.IModifyToolbar
import com.xently.xui.utils.ui.fragment.hideKeyboard

open class Fragment : androidx.fragment.app.Fragment(), DateSetListener, TimeSetListener {

    protected val editable: Editable.Factory by lazy { Editable.Factory.getInstance() }

    private val backPressDispatcher by lazy { requireActivity().onBackPressedDispatcher }

    private var backPressCallback: OnBackPressedCallback? = null

    private var iModifyToolbar: IModifyToolbar? = null

    /**
     * What to set as title of [Toolbar]
     * @see IModifyToolbar
     */
    protected open val toolbarTitle: String? get() = null

    /**
     * What to set as title of [Toolbar]
     * @see IModifyToolbar
     */
    protected open val toolbarSubTitle: String? get() = null

    /**
     * Option to hide/show [Toolbar]. Default behaviour is **true** (show)
     * @see IModifyToolbar
     */
    protected open val showToolbar: Boolean get() = true

    protected open val toolbarUpIcon: Int?
        @DrawableRes get() = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        backPressCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() = onBackPressed()
        }
        backPressDispatcher.addCallback(this, backPressCallback!!)

        setHasOptionsMenu(true)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is IModifyToolbar) iModifyToolbar = context
    }

    override fun onDetach() {
        iModifyToolbar = null
        super.onDetach()
    }

    override fun onResume() {
        super.onResume()
        iModifyToolbar?.onModifyToolbar(
            toolbarTitle,
            toolbarSubTitle,
            toolbarUpIcon,
            showToolbar
        )
    }

    @CallSuper
    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        item.onNavDestinationSelected(findNavController()) || super.onOptionsItemSelected(item)

    override fun onDateSet(date: String, tag: String?) = Unit

    override fun onTimeSet(time: String, tag: String?) = Unit

    /**
     * What to do when system/phone's **_back-button_** is pressed
     */
    open fun onBackPressed() {
        Log.show("BaseFragment", "onBackPressed: ${this::class.java.name}")
        backPressCallback?.isEnabled = false
        backPressDispatcher.onBackPressed()
    }

    /**
     * Shows a date picker dialog and initialized by default to the presumed date shown on [et] or
     * default current date(if it is not a date in expected format)
     */
    protected fun <T : TextView> showDatePicker(et: T, tag: String) =
        DatePickerDialog.getInstance(null, et.text).apply {
            initialDate = et.text
            dialogButtonText =
                fromStringResource(et.context, R.string.xui_picker_dialog_positive_button)
            dateSetListener = this@Fragment
        }.show(childFragmentManager, tag)

    /**
     * Shows a time picker dialog and initialized by default to the presumed time shown on [et] or
     * default current time(if it is not a time in expected format)
     */
    protected fun <T : TextView> showTimePicker(et: T, tag: String) =
        TimePickerDialog.getInstance(null, initialTime = et.text).apply {
            initialTime = et.text
            dialogButtonText =
                fromStringResource(et.context, R.string.xui_picker_dialog_positive_button)
            timeSetListener = this@Fragment
        }.show(childFragmentManager, tag)

    protected fun TextInputLayout.clickIconToShowDatePicker(tag: String, icon: Icon = END) {
        when (icon) {
            END -> setEndIconOnClickListener {
                hideKeyboard()
                if (editText != null) showDatePicker(editText!!, tag)
            }
            START -> setStartIconOnClickListener {
                hideKeyboard()
                if (editText != null) showDatePicker(editText!!, tag)
            }
        }
    }

    protected fun TextInputLayout.clickIconToShowTimePicker(tag: String, icon: Icon = END) {
        when (icon) {
            END -> setEndIconOnClickListener {
                hideKeyboard()
                if (editText != null) showTimePicker(editText!!, tag)
            }
            START -> setStartIconOnClickListener {
                hideKeyboard()
                if (editText != null) showTimePicker(editText!!, tag)
            }
        }
    }
}