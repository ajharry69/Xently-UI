package com.xently.xui

import android.widget.EditText
import com.xently.dialog.ButtonText
import com.xently.dialog.DatePickerDialog
import com.xently.dialog.TimePickerDialog

/**
 * Contains helper methods to help show [DatePickerDialog] and [TimePickerDialog]
 */
abstract class DateTimePickerFragment : Fragment(),
    DatePickerDialog.OnDatePickerDialogDateSetListener,
    TimePickerDialog.OnTimePickerDialogTimeSetListener {

    protected fun <T : EditText> showDatePicker(et: T, tag: String) =
        DatePickerDialog.getInstance(null, et.text).apply {
            this.initialDate = et.text
            this.dialogButtonText = ButtonText.fromStringResource(
                this@DateTimePickerFragment.requireContext(),
                R.string.xui_date_picker_dialog_positive_button
            )
            this.onDatePickerDialogDateSetListener = this@DateTimePickerFragment
        }.show(childFragmentManager, tag)

    protected fun <T : EditText> showTimePicker(et: T, tag: String) =
        TimePickerDialog.getInstance(null, initialTime = et.text).apply {
            this.initialTime = et.text
            this.dialogButtonText = ButtonText.fromStringResource(
                this@DateTimePickerFragment.requireContext(),
                R.string.xui_date_picker_dialog_positive_button
            )
            this.onTimePickerDialogTimeSetListener = this@DateTimePickerFragment
        }.show(childFragmentManager, tag)

    override fun onDatePickerDialogDateSet(date: String, tag: String?) = Unit

    override fun onTimePickerDialogTimeSet(time: String, tag: String?) = Unit
}