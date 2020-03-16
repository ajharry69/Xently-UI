package com.xently.xui

import android.widget.EditText
import com.xently.dialog.ButtonText
import com.xently.xui.dialog.DatePickerDialog
import com.xently.xui.dialog.TimePickerDialog

/**
 * Contains helper methods to help show [DatePickerDialog] and [TimePickerDialog]
 */
abstract class DateTimePickerFragment : Fragment(), DatePickerDialog.DateSetListener,
    TimePickerDialog.TimeSetListener {

    protected fun <T : EditText> showDatePicker(et: T, tag: String) =
        DatePickerDialog.getInstance(null, et.text).apply {
            this.initialDate = et.text
            this.dialogButtonText = ButtonText.fromStringResource(
                this@DateTimePickerFragment.requireContext(),
                R.string.xui_picker_dialog_positive_button
            )
            this.dateSetListener = this@DateTimePickerFragment
        }.show(childFragmentManager, tag)

    protected fun <T : EditText> showTimePicker(et: T, tag: String) =
        TimePickerDialog.getInstance(null, initialTime = et.text).apply {
            this.initialTime = et.text
            this.dialogButtonText = ButtonText.fromStringResource(
                this@DateTimePickerFragment.requireContext(),
                R.string.xui_picker_dialog_positive_button
            )
            this.timeSetListener = this@DateTimePickerFragment
        }.show(childFragmentManager, tag)

    override fun onDateSet(date: String, tag: String?) = Unit

    override fun onTimeSet(time: String, tag: String?) = Unit
}