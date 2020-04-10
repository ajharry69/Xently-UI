package com.xently.ui.demo.ui.core

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.xently.ui.demo.R
import com.xently.ui.demo.adapters.EmployeeListAdapter
import com.xently.ui.demo.data.Employee
import com.xently.ui.demo.viewmodels.EmployeeViewModel
import com.xently.xui.ListFragment
import com.xently.xui.dialog.DialogFragment
import com.xently.xui.utils.ui.fragment.onCreateDeletionDialog
import com.xently.xui.utils.ui.fragment.showSnackBar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class EmployeeListFragment : ListFragment<Employee>() {

    private val listAdapter: EmployeeListAdapter by lazy {
        EmployeeListAdapter().apply {
            listItemClickListener = this@EmployeeListFragment
        }
    }

    abstract val viewModel: EmployeeViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fab.setImageResource(R.drawable.ic_action_add_employee)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        onRefreshRequested(false)
        viewModel.observableEmployeeListRefreshEvent.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer
            onRefreshEvent(it)
        })
    }

    override fun onCreateRecyclerView(recyclerView: RecyclerView): RecyclerView {
        return super.onCreateRecyclerView(recyclerView).apply {
            adapter = listAdapter
        }
    }

    override fun onFabClickListener(context: Context): View.OnClickListener? {
        return View.OnClickListener {
            viewModel.viewModelScope.launch(Dispatchers.Default) {
                viewModel.hireEmployee()
            }
        }
    }

    override fun onRefreshRequested(forced: Boolean) {
        viewModel.viewModelScope.launch(Dispatchers.Default) {
            viewModel.getEmployeeList()
        }
    }

    override fun onListItemClick(model: Employee, view: View) {
        showSnackBar(model.toString(), Snackbar.LENGTH_LONG, getString(R.string.fire)) {
            onCreateDeletionDialog(R.string.confirm_delete, model.name).apply {
                buttonClickListener = object : DialogFragment.ButtonClickListener {
                    override fun onPositiveButtonClick(
                        dialog: DialogInterface,
                        index: Int,
                        tag: String?
                    ) {
                        viewModel.fireEmployee(model)?.apply {
                            message?.let { it ->
                                this@EmployeeListFragment.showSnackBar(
                                    it,
                                    Snackbar.LENGTH_LONG
                                )
                            }
                        }
                        super.onPositiveButtonClick(dialog, index, tag)
                    }
                }
            }.show(childFragmentManager, "DialogTag_DeleteEmployee")
        }
    }

    protected fun observeEmployeeList(query: String?) {
        viewModel.getObservableEmployeeList(query).observe(viewLifecycleOwner, Observer {
            onObservableListChanged(it)

            listAdapter.submitList(it)
        })
    }
}