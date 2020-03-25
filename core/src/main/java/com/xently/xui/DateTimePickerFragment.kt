package com.xently.xui

import android.widget.TextView
import com.xently.dialog.ButtonText
import com.xently.xui.dialog.DatePickerDialog
import com.xently.xui.dialog.DatePickerDialog.DateSetListener
import com.xently.xui.dialog.TimePickerDialog
import com.xently.xui.dialog.TimePickerDialog.TimeSetListener

/**
 * Contains helper methods to help show [DatePickerDialog] and [TimePickerDialog]
 */
abstract class DateTimePickerFragment : Fragment(), DateSetListener, TimeSetListener {

    /**
     * Shows a date picker dialog and initialized by default to the presumed date shown on [et] or
     * default current date(if it is not a date in expected format)
     */
    protected fun <T : TextView> showDatePicker(et: T, tag: String) =
        DatePickerDialog.getInstance(null, et.text).apply {
            initialDate = et.text
            dialogButtonText = ButtonText.fromStringResource(
                this@DateTimePickerFragment.requireContext(),
                R.string.xui_picker_dialog_positive_button
            )
            dateSetListener = this@DateTimePickerFragment
        }.show(childFragmentManager, tag)

    /**
     * Shows a time picker dialog and initialized by default to the presumed time shown on [et] or
     * default current time(if it is not a time in expected format)
     */
    protected fun <T : TextView> showTimePicker(et: T, tag: String) =
        TimePickerDialog.getInstance(null, initialTime = et.text).apply {
            initialTime = et.text
            dialogButtonText = ButtonText.fromStringResource(
                this@DateTimePickerFragment.requireContext(),
                R.string.xui_picker_dialog_positive_button
            )
            timeSetListener = this@DateTimePickerFragment
        }.show(childFragmentManager, tag)

    override fun onDateSet(date: String, tag: String?) = Unit

    override fun onTimeSet(time: String, tag: String?) = Unit
}