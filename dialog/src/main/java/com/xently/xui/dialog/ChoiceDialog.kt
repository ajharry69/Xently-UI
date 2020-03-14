package com.xently.xui.dialog

import android.app.Dialog
import android.content.DialogInterface
import androidx.annotation.ArrayRes
import androidx.annotation.StyleRes
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.xently.dialog.ChoiceDialogParams

/**
 * @see DialogFragment
 */
class ChoiceDialog : DialogFragment() {

    enum class Type { DEFAULT, SINGLE, MULTIPLE }

    /**
     * Callback/listener to respond to [ChoiceDialog] item selections
     * @see onItemSelected
     */
    interface ItemSelectedListener {
        /**
         * Callback method to respond to [ChoiceDialog] item selections
         * @param selectedItems list of items selected from a **MultipleChoiceItems**. Only
         * applicable in **MultipleChoiceItems**. Still does not work as expected
         * @param tag set when `Dialog.show(fm: FragmentManager, **tag**: String)` was called. This
         * can be used to identify **same dialog type** initiators. Ex. if a **DatePickerDialog**
         * was shown from different views in the same screen or fragment, inorder to respond to
         * their **button** click you will need to identify them with their **transaction** tags to
         * be able to separate button click behaviours
         * @see ItemSelectedListener
         */
        fun onItemSelected(
            dialog: DialogInterface,
            index: Int,
            selectedItems: List<CharSequence>,
            tag: String?
        )
    }

    /**
     * @see ItemSelectedListener
     */
    var itemSelectedListener: ItemSelectedListener? = null

    /**
     * Type of [ChoiceDialog]
     */
    var type: Type = defaultType

    /**
     * A resource pointing to a List of string to show as choices
     */
    @ArrayRes
    var entriesFromResource: Int? = null

    /**
     * List of string to show as choices
     */
    var entries: Array<out CharSequence> = emptyArray()

    override fun setAsDialogProperties(dialog: MaterialAlertDialogBuilder) {

        // Try to initialize with [entriesFromResource] if condition is true
        if (entries.isNullOrEmpty()) this.entries =
            entriesFromResource?.let { requireActivity().resources.getStringArray(it) }
                ?: emptyArray()

        // If still empty do not proceed
        if (entries.isNullOrEmpty()) return

        with(dialog) {
            when (type) {
                Type.DEFAULT -> {
                    setItems(entries) { dialog, which ->
                        val selected =
                            (dialog as AlertDialog).listView.getItemAtPosition(which) as String?
                        itemSelectedListener?.onItemSelected(
                            dialog = dialog,
                            index = which,
                            selectedItems = selected?.let { listOf(it) } ?: listOf(),
                            tag = tag
                        )
                    }
                }
                Type.SINGLE -> {
                    setSingleChoiceItems(entries, 0) { dialog, which ->
                        val selected =
                            (dialog as AlertDialog).listView.getItemAtPosition(which) as String?
                        itemSelectedListener?.onItemSelected(
                            dialog = dialog,
                            index = which,
                            selectedItems = selected?.let { listOf(it) } ?: listOf(),
                            tag = tag
                        )
                    }
                }
                Type.MULTIPLE -> {
                    val selectedItems: MutableList<String> = ArrayList()
                    setMultiChoiceItems(entries, null) { dialog, which, isChecked ->
                        val selected =
                            (dialog as AlertDialog).listView.getItemAtPosition(which) as String?

                        if (!selected.isNullOrBlank()) {
                            if (isChecked) {
                                // Add checked item to list of selected items
                                selectedItems.add(selected)
                            } else {
                                // Remove unchecked item from list of selected items
                                if (selectedItems.contains(selected)) selectedItems.remove(selected)
                            }
                        }

                        itemSelectedListener?.onItemSelected(
                            dialog = dialog,
                            index = which,
                            selectedItems = selectedItems,
                            tag = tag
                        )
                    }
                }
            }
        }
    }

    companion object {

        private val defaultType = Type.DEFAULT

        /**
         * Creates an instance of the [ChoiceDialog] with [entries] to show as [Dialog] content
         * @param entries array of items to show on [Dialog]'s content area
         * @param theme What to use as the alert [Dialog]'s theme. If `null`, default app theme is
         * applied
         * @see DialogFragment
         */
        fun getInstance(
            entries: Iterable<CharSequence>,
            params: ChoiceDialogParams? = null,
            @StyleRes theme: Int? = null,
            type: Type = defaultType
        ): ChoiceDialog = ChoiceDialog().apply {
            val toList = entries.toList()
            if (!toList.isNullOrEmpty()) this.entries = toList.toTypedArray()
            this.type = type
            this.dialogTitle = params?.title
            this.dialogThemeFromResource = theme
            // Do not show buttons for list choice dialog
            if (type != Type.DEFAULT) params?.buttonText?.let { this.dialogButtonText = it }
        }

        /**
         * Creates an instance of the [ChoiceDialog] with [entries] to show as [Dialog] content
         * @param theme What to use as the alert [Dialog]'s theme. If `null`, default app theme is
         * applied
         * @see DialogFragment
         */
        fun getInstance(
            entries: Array<out CharSequence>,
            params: ChoiceDialogParams? = null,
            @StyleRes theme: Int? = null,
            type: Type = defaultType
        ): ChoiceDialog = getInstance(entries.toList(), params, theme, type)

        /**
         * Creates an instance of the [ChoiceDialog] with [entries] to show as [Dialog] content
         * @param theme What to use as the alert [Dialog]'s theme. If `null`, default app theme is
         * applied
         * @see DialogFragment
         */
        fun getInstance(
            @ArrayRes entries: Int,
            @StyleRes theme: Int? = null,
            type: Type = defaultType,
            params: ChoiceDialogParams? = null
        ): ChoiceDialog = with(getInstance(emptyList(), params, theme, type)) {
            return@with this.apply {
                this.entriesFromResource = entries
                this.type = type
                this.dialogThemeFromResource = theme
            }
        }
    }
}