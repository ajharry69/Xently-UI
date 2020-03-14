package com.xently.ui.demo

import android.content.DialogInterface
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.xently.dialog.ButtonText
import com.xently.dialog.ChoiceDialogParams
import com.xently.dialog.DateTimePickerParams
import com.xently.dialog.DialogParams
import com.xently.ui.demo.databinding.MainActivityBinding
import com.xently.xui.SearchableActivity
import com.xently.xui.dialog.*

@Suppress("UNUSED_PARAMETER")
class MainActivity : SearchableActivity(), ChoiceDialog.OnChoiceDialogItemSelectedListener,
    DialogFragment.DialogButtonClickListener {

    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.cancel_activity -> {
                showSnackBar("Clicked activity: ${item.title}")
                true
            }
            R.id.okay_activity -> {
                showSnackBar("Clicked activity: ${item.title}")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDialogPositiveButtonClick(
        dialog: DialogInterface,
        index: Int,
        tag: String?
    ) {
        tag?.let { showSnackBar(it, Snackbar.LENGTH_LONG) }
        when (tag) {
            MESSAGE_DIALOG_TAG -> {
                // Do something when this message dialog's positive button is
                // clicked...
            }
            ANOTHER_MESSAGE_DIALOG_TAG -> {
                // Do something when another message dialog's positive button
                // is clicked...
            }
        }
        dialog.dismiss()
    }

    override fun onChoiceDialogItemSelected(
        dialog: DialogInterface,
        index: Int,
        selectedItems: List<CharSequence>,
        tag: String?,
        choiceDialog: ChoiceDialog
    ) {
        val choiceAtIndex = choiceDialogData[index]
        when (tag) {
            LIST_CHOICE_DIALOG_TAG -> {
                showSnackBar(
                    "Selected Item = Item at $index? ${selectedItems.contains(
                        choiceAtIndex
                    ) && selectedItems[0] == choiceAtIndex}", Snackbar.LENGTH_LONG
                )
            }
            SINGLE_CHOICE_DIALOG_TAG -> {
                showSnackBar(
                    "Selected Item = Item at $index? ${selectedItems.contains(
                        choiceAtIndex
                    ) && selectedItems[0] == choiceAtIndex}", Snackbar.LENGTH_LONG
                )
                dialog.dismiss()
            }
            MULTIPLE_CHOICE_DIALOG_TAG -> {
                showSnackBar("Selected: $selectedItems")
            }
        }
    }

    fun showMessageDialogOne(view: View) {
        MessageDialog.getInstance(
            DialogParams.fromStringResource(
                this,
                R.string.md_title,
                R.string.md_message,
                ButtonText.fromStringResource(this, android.R.string.ok, android.R.string.cancel)
            )
        ).apply {
            setDialogStyleAndLaunchMode(
                DialogFragment.Style.NORMAL,
                DialogFragment.LaunchMode.NORMAL
            )
            buttonClickListener = this@MainActivity
        }.show(supportFragmentManager, MESSAGE_DIALOG_TAG)
    }

    fun showMessageDialogTwo(view: View) {
        MessageDialog.getInstance(
            DialogParams(
                null,
                "Message dialog with out title",
                ButtonText(getString(R.string.okay), getString(android.R.string.cancel))
            )
        ).apply {
            buttonClickListener = this@MainActivity
        }.show(supportFragmentManager, ANOTHER_MESSAGE_DIALOG_TAG)
    }

    fun showCustomLayoutDialog(view: View) {
        CustomDialog().show(supportFragmentManager, CUSTOM_DIALOG_TAG)
    }

    fun showFullScreenDialog(view: View) {
        FullScreenDialog().apply {
            buttonClickListener = object : DialogFragment.DialogButtonClickListener {
                override fun onDialogPositiveButtonClick(
                    dialog: DialogInterface,
                    index: Int,
                    tag: String?
                ) {
                    showSnackBar("Fullscreen dialog of tag: $tag and index: $index")
                    dialog.dismiss()
                }
            }
        }.show(supportFragmentManager, FULLSCREEN_DIALOG_TAG)
    }

    fun showMultiChoiceDialog(view: View) {
        ChoiceDialog.getInstance(
            R.array.choice_dialog_entries,
            params = ChoiceDialogParams("Title", ButtonText(getString(R.string.positive_button))),
            type = ChoiceDialog.Type.MULTIPLE
        ).apply {
            dialogStyle = DialogFragment.Style.NO_TITLE
            dialogAnimationFromResource = R.style.AppTheme_Slide
//            type = ChoiceDialog.Type.MULTIPLE
//            entries = choiceDialogData
//            entriesFromResource = R.array.choice_dialog_entries
            onChoiceDialogItemSelectedListener = this@MainActivity
        }.show(supportFragmentManager, MULTIPLE_CHOICE_DIALOG_TAG)
    }

    fun showSingleChoiceDialog(view: View) {
        ChoiceDialog.getInstance(
            choiceDialogData,
            ChoiceDialogParams("Title", ButtonText("Hello")),
            type = ChoiceDialog.Type.SINGLE
        ).apply {
            onChoiceDialogItemSelectedListener = this@MainActivity
        }.show(supportFragmentManager, SINGLE_CHOICE_DIALOG_TAG)
    }

    fun showListChoiceDialog(view: View) {
        ChoiceDialog.getInstance(
            choiceDialogData.toList()
        ).apply {
            onChoiceDialogItemSelectedListener = this@MainActivity
        }.show(supportFragmentManager, LIST_CHOICE_DIALOG_TAG)
    }

    fun showTimePickerDialog(view: View) {
        // Creates shows a time picker with time set to 10:23
        TimePickerDialog.getInstance(
            params = DateTimePickerParams(
                null,
                ButtonText("Set1", "Cancel")
            ), initialTime = "   10:23 ", is24Hours = true
        ).apply {
            onTimePickerDialogTimeSetListener = object :
                TimePickerDialog.OnTimePickerDialogTimeSetListener {
                override fun onTimePickerDialogTimeSet(time: String, tag: String?) {
                    // Do something with the returned time...
                    showSnackBar(time)
                }
            }
        }.show(supportFragmentManager, TIME_PICKER_DIALOG_TAG)
    }

    fun showDatePickerDialog(view: View) {
        // Creates shows a date picker with date set to 2016-05-18
        DatePickerDialog.getInstance().apply {
            onDatePickerDialogDateSetListener = object :
                DatePickerDialog.OnDatePickerDialogDateSetListener {
                override fun onDatePickerDialogDateSet(date: String, tag: String?) {
                    showSnackBar(date)
                }
            }
        }.show(supportFragmentManager, DATE_PICKER_DIALOG_TAG)
    }

    /**
     * Shows [Snackbar] for given `duration`
     * @param duration: @see Snackbar.getDuration
     * @param message: [Snackbar] message
     * @param actionButtonClick: Callback for responding to [Snackbar] action button click
     * @param actionButtonText: Label text shown on [Snackbar]s action button
     */
    fun showSnackBar(
        message: String,
        duration: Int = Snackbar.LENGTH_SHORT, actionButtonText: String? = null,
        actionButtonClick: ((snackBar: Snackbar) -> Unit)? = null
    ): Snackbar {
        val snackbar = Snackbar.make(binding.root, message, duration)
        with(snackbar) {
            if (actionButtonText != null) setAction(actionButtonText) {
                actionButtonClick?.invoke(this)
            }
            if (!this.isShownOrQueued) show()
        }
        return snackbar
    }

    companion object {
        val choiceDialogData = arrayOf("Create", "Read", "Update", "Delete")
        val TAG: String = MainActivity::class.java.simpleName
        private val MULTIPLE_CHOICE_DIALOG_TAG = "${TAG}_MULTIPLE_CHOICE_DIALOG_TAG"
        private val SINGLE_CHOICE_DIALOG_TAG = "${TAG}_SINGLE_CHOICE_DIALOG_TAG"
        private val LIST_CHOICE_DIALOG_TAG = "${TAG}_LIST_CHOICE_DIALOG_TAG"
        private val TIME_PICKER_DIALOG_TAG = "${TAG}_TIME_PICKER_DIALOG_TAG"
        private val DATE_PICKER_DIALOG_TAG = "${TAG}_DATE_PICKER_DIALOG_TAG"
        private val MESSAGE_DIALOG_TAG = "${TAG}_MESSAGE_DIALOG_TAG"
        private val ANOTHER_MESSAGE_DIALOG_TAG = "${TAG}_ANOTHER_MESSAGE_DIALOG_TAG"
        private val CUSTOM_DIALOG_TAG = "${TAG}_CUSTOM_DIALOG_TAG"
        private val FULLSCREEN_DIALOG_TAG = "${TAG}_FULLSCREEN_DIALOG_TAG"
    }

}
