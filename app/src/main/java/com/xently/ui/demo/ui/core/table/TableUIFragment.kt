package com.xently.ui.demo.ui.core.table

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.xently.ui.demo.data.Employee
import com.xently.xui.DataTableFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.random.Random

class TableUIFragment : DataTableFragment<Employee>(EmployeeTableViewModel()) {

    private val viewModel: TableUIViewModel by viewModels {
        TableUIViewModelFactory()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        onRefreshRequested(false)
        with(viewModel) {
            getObservableEmployeeList(null).observe(viewLifecycleOwner, Observer {
                onObservableListChanged(it)

                tableAdapter.submitList(it)
            })
            observableEmployeeListRefreshEvent.observe(viewLifecycleOwner, Observer {
                if (it == null) return@Observer
                onRefreshEvent(it)
            })
        }
    }

    override fun onRefreshRequested(forced: Boolean) {
        viewModel.viewModelScope.launch(Dispatchers.Default) {
            val limit = Random.nextInt(50, 100)
            viewModel.getEmployeeList(limit = limit)
        }
    }

    companion object {
        fun newInstance(): TableUIFragment = TableUIFragment()
    }
}

class TableUIFragmentFactory : FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment =
        TableUIFragment.newInstance()
}