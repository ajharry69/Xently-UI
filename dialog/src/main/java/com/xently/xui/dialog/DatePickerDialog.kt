package com.xently.xui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.DatePicker
import androidx.annotation.StyleRes
import com.xently.dialog.ButtonText
import com.xently.dialog.DateTimePickerParams
import com.xently.xui.dialog.utils.DateFormat
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import java.util.*

/**
 * @see DialogFragment
 */
class DatePickerDialog private constructor() : DialogFragment(),
    android.app.DatePickerDialog.OnDateSetListener {

    /**
     * Callback/listener to respond to date picked from [DatePickerDialog]
     * @see dateSetListener
     */
    interface DateSetListener {

        /**
         * Callback method to respond to date picked from [DatePickerDialog]
         * @param tag Used to identify different [DatePickerDialog]s sharing the same
         * [DateSetListener]
         * @see DateSetListener
         */
        fun onDateSet(date: String, tag: String?)
    }

    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
        dateSetListener?.onDateSet(
            DateTime().withDate(p1, p2 + 1, p3).toString(dateTimeFormatter()), tag
        )
    }

    private fun dateTimeFormatter(): DateTimeFormatter? {
        val dateFormat = this.returnDateFormat as String?

        return if (dateFormat == null) {
            DateTimeFormat.longDate()
        } else {
            DateTimeFormat.forPattern(dateFormat)
        }
    }

    var initialDate: CharSequence? = null

    var returnDateFormat: String = DateFormat.dateOnly()

    /**
     * @see DateSetListener
     */
    var dateSetListener: DateSetListener? = null

    override fun onDetach() {
        super.onDetach()
        dateSetListener = null
    }

    override fun setDialogButtonText(context: Context): ButtonText? =
        ButtonText.fromStringResource(
            context,
            R.string.xui_dialog_picker_dialog_positive_button_text
        )

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val calender = GregorianCalendar.getInstance().apply {
            time = try {
                DateTime.parse(initialDate.toString(), dateTimeFormatter()).toDate()
            } catch (ex: Exception) {
                Date()
            }
        }

        val year = calender[Calendar.YEAR]
        val monthOfYear = calender[Calendar.MONTH]
        val dayOfMonth = calender[Calendar.DAY_OF_MONTH]

        val datePickerDialog = dialogThemeFromResource?.let {
            android.app.DatePickerDialog(requireContext(), it, this, year, monthOfYear, dayOfMonth)
        } ?: android.app.DatePickerDialog(requireContext(), this, year, monthOfYear, dayOfMonth)

        with(datePickerDialog) {
            dialogIcon?.let {
                setIcon(it)
            }
            dialogTitle?.let {
                setTitle(it)
            }
            dialogButtonText?.positive?.let {
                setButton(android.app.DatePickerDialog.BUTTON_POSITIVE, it, this@with)
            }
            dialogButtonText?.negative?.let {
                setButton(android.app.DatePickerDialog.BUTTON_NEGATIVE, it, this@with)
            }
            dialogButtonText?.neutral?.let {
                setButton(android.app.DatePickerDialog.BUTTON_NEUTRAL, it, this@with)
            }
        }
        return datePickerDialog
    }

    companion object {

        /**
         * @param initialDate Date shown on date-picker dialog when it's shown. If `null`, current
         * date is used. **Date's format/pattern MUST follow the same pattern as [format] provided
         * otherwise it's considered invalid and the current date is used**
         * @param format The format in which the date is to be returned. Default is [DateFormat.dateOnly]
         * @see DialogFragment
         */
        fun getInstance(
            params: DateTimePickerParams? = null,
            initialDate: CharSequence? = null,
            format: String? = DateFormat.dateOnly(),
            @StyleRes theme: Int? = null
        ): DatePickerDialog =
            DatePickerDialog().apply {
                var date = initialDate

                if (!date.isNullOrEmpty()) {
                    while (date!!.endsWith(' ')) date = date.trimEnd()

                    while (date!!.startsWith(' ')) date = date.trimStart()
                }

                this.initialDate = date
                if (format != null) this.returnDateFormat = format
                this.dialogTitle = params?.title
                this.dialogThemeFromResource = theme
                params?.buttonText?.let { this.dialogButtonText = it }
            }
    }

}
