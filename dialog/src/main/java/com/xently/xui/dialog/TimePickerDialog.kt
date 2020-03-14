package com.xently.xui.dialog


import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.TimePicker
import androidx.annotation.DrawableRes
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
class TimePickerDialog : DialogFragment(), android.app.TimePickerDialog.OnTimeSetListener {

    /**
     * Callback/listener to respond to time picked from [TimePickerDialog]
     * @see onTimeSet
     */
    interface TimeSetListener {
        /**
         * Callback method to respond to time picked from [TimePickerDialog]
         * @param tag Used to identify different [TimePickerDialog]s sharing the same
         * [TimeSetListener]
         * @see TimeSetListener
         */
        fun onTimeSet(time: String, tag: String?)
    }

    override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
        val time = DateTime().withTime(p1, p2, 0, 0)
        timeSetListener?.onTimeSet(
            time.toString(dateTimeFormatter()), tag
        )
    }

    private fun dateTimeFormatter(): DateTimeFormatter? {
        val timeFormat = returnTimeFormat as String?

        return if (timeFormat == null) {
            DateTimeFormat.shortTime()
        } else {
            DateTimeFormat.forPattern(timeFormat)
        }
    }

    var initialTime: CharSequence? = null

    var is24Hours: Boolean = false

    var returnTimeFormat: String = if (is24Hours) {
        DateFormat.TIME_HM_24_HRS
    } else {
        DateFormat.TIME_HM_12_HRS
    }

    /**
     * @see TimeSetListener
     */
    var timeSetListener: TimeSetListener? = null

    override fun onDetach() {
        super.onDetach()
        timeSetListener = null
    }

    override fun setDialogButtonText(context: Context): ButtonText? =
        ButtonText.fromStringResource(
            context,
            R.string.xui_dialog_picker_dialog_positive_button_text
        )

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        val is24hours = is24Hours ?: DateFormat.is24HourFormat(context)

        val calender = GregorianCalendar.getInstance().apply {
            time = try {
                DateTime.parse(initialTime.toString(), dateTimeFormatter()).toDate()
            } catch (ex: Exception) {
                Date()
            }
        }

        val hours = if (is24Hours) {
            calender[Calendar.HOUR_OF_DAY]
        } else {
            calender[Calendar.HOUR]
        }
        val minutes = calender[Calendar.MINUTE]

        val timePickerDialog = dialogThemeFromResource?.let {
            android.app.TimePickerDialog(context, it, this, hours, minutes, is24Hours)
        } ?: android.app.TimePickerDialog(context, this, hours, minutes, is24Hours)

        with(timePickerDialog) {
            dialogIcon?.let { setIcon(it) }
            dialogTitle?.let { setTitle(it) }
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
        return timePickerDialog
    }

    companion object {

        private val defaultTimeFormat: (is24Hours: Boolean) -> String = {
            if (it) DateFormat.TIME_HM_24_HRS else DateFormat.TIME_HM_12_HRS
        }

        /**
         * @param icon what to use as [Dialog]'s **icon**
         * @param theme What to use as the alert [Dialog]'s theme. If `null`, default app theme is
         * applied
         * @param initialTime Time shown on time-picker dialog when it's shown. If `null`, current
         * time is used **Time's format/pattern MUST follow the same pattern as [format] provided
         * otherwise it's considered invalid and the current time is used**
         * @param format The format in which the date is to be returned. Default is
         * [com.xently.xui.dialog.utils.DateFormat.TIME_HM_12_HRS] if [is24Hours] = **`false`**
         * otherwise [com.xently.xui.dialog.utils.DateFormat.TIME_HM_24_HRS]
         * @see DialogFragment
         */
        fun getInstance(
            params: DateTimePickerParams? = null,
            initialTime: CharSequence? = null,
            is24Hours: Boolean = false,
            format: String = defaultTimeFormat(is24Hours),
            @DrawableRes
            icon: Int? = null,
            @StyleRes theme: Int? = null
        ): TimePickerDialog =
            TimePickerDialog().apply {

                var time = initialTime

                if (!time.isNullOrEmpty()) {
                    while (time!!.endsWith(' ')) time = time.trimEnd()

                    while (time!!.startsWith(' ')) time = time.trimStart()
                }

                this.initialTime = time
                this.is24Hours = is24Hours
                this.returnTimeFormat = format
                this.dialogTitle = params?.title
                this.dialogIconFromResource = icon
                this.dialogThemeFromResource = theme
                params?.buttonText?.let { this.dialogButtonText = it }
            }
    }

}
