package com.xently.ui.demo.handlers

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.xently.dialog.ButtonText
import com.xently.dialog.ChoiceDialogParams
import com.xently.dialog.DateTimePickerParams
import com.xently.dialog.DialogParams
import com.xently.ui.demo.ui.dialog.custom.CustomDialog
import com.xently.ui.demo.ui.dialog.custom.FullScreenDialog
import com.xently.ui.demo.R
import com.xently.ui.demo.ui.dialog.DialogUIFragment.Companion.ANOTHER_MESSAGE_DIALOG_TAG
import com.xently.ui.demo.ui.dialog.DialogUIFragment.Companion.CUSTOM_DIALOG_TAG
import com.xently.ui.demo.ui.dialog.DialogUIFragment.Companion.DATE_PICKER_DIALOG_TAG
import com.xently.ui.demo.ui.dialog.DialogUIFragment.Companion.FULLSCREEN_DIALOG_TAG
import com.xently.ui.demo.ui.dialog.DialogUIFragment.Companion.LIST_CHOICE_DIALOG_TAG
import com.xently.ui.demo.ui.dialog.DialogUIFragment.Companion.MESSAGE_DIALOG_TAG
import com.xently.ui.demo.ui.dialog.DialogUIFragment.Companion.MULTIPLE_CHOICE_DIALOG_TAG
import com.xently.ui.demo.ui.dialog.DialogUIFragment.Companion.SINGLE_CHOICE_DIALOG_TAG
import com.xently.ui.demo.ui.dialog.DialogUIFragment.Companion.TIME_PICKER_DIALOG_TAG
import com.xently.ui.demo.ui.dialog.DialogUIFragment.Companion.choiceDialogData
import com.xently.xui.dialog.*
import com.xently.xui.dialog.ChoiceDialog.ItemSelectedListener
import com.xently.xui.dialog.DatePickerDialog.DateSetListener
import com.xently.xui.dialog.DialogFragment.ButtonClickListener
import com.xently.xui.dialog.TimePickerDialog.TimeSetListener

class ViewEventHandlers(
    private val context: Context,
    private val fm: FragmentManager,
    private val buttonClickCallback: ButtonClickListener,
    private val itemSelectedCallback: ItemSelectedListener,
    private val dateSetCallback: DateSetListener,
    private val timeSetCallback: TimeSetListener
) {

    fun showMessageDialogOne() {
        MessageDialog.getInstance(
            DialogParams.fromStringResource(
                context,
                R.string.md_title,
                R.string.md_message,
                ButtonText.fromStringResource(
                    context,
                    android.R.string.ok,
                    android.R.string.cancel
                )
            )
        ).apply {
            setDialogStyleAndLaunchMode(
                DialogFragment.Style.NORMAL,
                DialogFragment.LaunchMode.NORMAL
            )
            buttonClickListener = buttonClickCallback
        }.show(fm, MESSAGE_DIALOG_TAG)
    }

    fun showMessageDialogTwo() {
        MessageDialog.getInstance(
            DialogParams(
                null,
                "Message dialog with out title",
                ButtonText(
                    context.getString(R.string.okay),
                    context.getString(android.R.string.cancel)
                )
            )
        ).apply {
            buttonClickListener = buttonClickCallback
        }.show(fm, ANOTHER_MESSAGE_DIALOG_TAG)
    }

    fun showCustomLayoutDialog() {
        CustomDialog()
            .show(fm, CUSTOM_DIALOG_TAG)
    }

    fun showFullScreenDialog() {
        FullScreenDialog().apply {
            buttonClickListener = buttonClickCallback
        }.show(fm, FULLSCREEN_DIALOG_TAG)
    }

    fun showMultiChoiceDialog() {
        ChoiceDialog.getInstance(
            R.array.choice_dialog_entries,
            params = ChoiceDialogParams(
                "Title",
                ButtonText(context.getString(R.string.positive_button))
            ),
            type = ChoiceDialog.Type.MULTIPLE
        ).apply {
            dialogStyle = DialogFragment.Style.NO_TITLE
            dialogAnimationFromResource = R.style.AppTheme_Slide
//            type = ChoiceDialog.Type.MULTIPLE
//            entries = choiceDialogData
//            entriesFromResource = R.array.choice_dialog_entries
            itemSelectedListener = itemSelectedCallback
        }.show(fm, MULTIPLE_CHOICE_DIALOG_TAG)
    }

    fun showSingleChoiceDialog() {
        ChoiceDialog.getInstance(
            choiceDialogData,
            ChoiceDialogParams("Title", ButtonText("Hello")),
            type = ChoiceDialog.Type.SINGLE
        ).apply {
            itemSelectedListener = itemSelectedCallback
        }.show(fm, SINGLE_CHOICE_DIALOG_TAG)
    }

    fun showListChoiceDialog() {
        ChoiceDialog.getInstance(
            choiceDialogData.toList()
        ).apply {
            itemSelectedListener = itemSelectedCallback
        }.show(fm, LIST_CHOICE_DIALOG_TAG)
    }

    fun showTimePickerDialog() {
        // Creates shows a time picker with time set to 10:23
        TimePickerDialog.getInstance(
            params = DateTimePickerParams(
                null,
                ButtonText(context.getString(R.string.custom_picker_positive_button), "Cancel")
            ), initialTime = "   10:23 ", is24Hours = true
        ).apply {
            timeSetListener = timeSetCallback
        }.show(fm, TIME_PICKER_DIALOG_TAG)
    }

    fun showDatePickerDialog() {
        // Creates shows a date picker with date set to 2016-05-18
        DatePickerDialog.getInstance().apply {
            dateSetListener = dateSetCallback
        }.show(fm, DATE_PICKER_DIALOG_TAG)
    }
}