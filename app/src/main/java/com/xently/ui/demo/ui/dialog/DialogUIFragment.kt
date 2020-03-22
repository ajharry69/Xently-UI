package com.xently.ui.demo.ui.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.xently.ui.demo.databinding.DialogUiFragmentBinding
import com.xently.ui.demo.handlers.ViewEventHandlers
import com.xently.xui.Fragment
import com.xently.xui.dialog.ChoiceDialog.ItemSelectedListener
import com.xently.xui.dialog.DatePickerDialog.DateSetListener
import com.xently.xui.dialog.DialogFragment.ButtonClickListener
import com.xently.xui.dialog.TimePickerDialog.TimeSetListener

class DialogUIFragment : Fragment(), ItemSelectedListener, ButtonClickListener, DateSetListener,
    TimeSetListener {

    private var _binding: DialogUiFragmentBinding? = null
    private val binding: DialogUiFragmentBinding
        get() = _binding!!

    private val viewModel: DialogUIViewModel by viewModels {
        DialogUIViewModelFactory()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogUiFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.clickHandler =
            ViewEventHandlers(requireContext(), childFragmentManager, this, this, this, this)
    }

    override fun onPositiveButtonClick(
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
            FULLSCREEN_DIALOG_TAG ->
                showSnackBar("Fullscreen dialog of tag: $tag and index: $index")
        }
        dialog.dismiss()
    }

    override fun onItemSelected(
        dialog: DialogInterface,
        index: Int,
        selectedItems: List<CharSequence>,
        tag: String?
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

    override fun onDateSet(date: String, tag: String?) {
        if (tag == DATE_PICKER_DIALOG_TAG) showSnackBar(date)
    }

    override fun onTimeSet(time: String, tag: String?) {
        if (tag == TIME_PICKER_DIALOG_TAG) showSnackBar(time)
    }

    companion object {
        val choiceDialogData = arrayOf("Create", "Read", "Update", "Delete")
        private val TAG: String = DialogUIFragment::class.java.simpleName
        val MULTIPLE_CHOICE_DIALOG_TAG = "${TAG}_MULTIPLE_CHOICE_DIALOG_TAG"
        val SINGLE_CHOICE_DIALOG_TAG = "${TAG}_SINGLE_CHOICE_DIALOG_TAG"
        val LIST_CHOICE_DIALOG_TAG = "${TAG}_LIST_CHOICE_DIALOG_TAG"
        val TIME_PICKER_DIALOG_TAG = "${TAG}_TIME_PICKER_DIALOG_TAG"
        val DATE_PICKER_DIALOG_TAG = "${TAG}_DATE_PICKER_DIALOG_TAG"
        val MESSAGE_DIALOG_TAG = "${TAG}_MESSAGE_DIALOG_TAG"
        val ANOTHER_MESSAGE_DIALOG_TAG = "${TAG}_ANOTHER_MESSAGE_DIALOG_TAG"
        val CUSTOM_DIALOG_TAG = "${TAG}_CUSTOM_DIALOG_TAG"
        val FULLSCREEN_DIALOG_TAG = "${TAG}_FULLSCREEN_DIALOG_TAG"
        fun newInstance() = DialogUIFragment()
    }
}

class DialogUIFragmentFactory : FragmentFactory() {
    override fun instantiate(
        classLoader: ClassLoader,
        className: String
    ): androidx.fragment.app.Fragment = DialogUIFragment.newInstance()
}
