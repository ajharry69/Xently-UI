package com.xently.ui.demo.ui.core.table

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.xently.ui.demo.data.Employee
import com.xently.xui.DataTableFragment
import kotlin.random.Random

class TableUIFragment : DataTableFragment<Employee>(EmployeeTableViewModel()) {

    private val viewModel: TableUIViewModel by viewModels {
        TableUIViewModelFactory()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onRefreshRequested(false)
        with(viewModel) {
            getObservableEmployeeList(null).observe(viewLifecycleOwner, Observer {
                onObservableListChanged(it)

                tableAdapter.submitList(it)
            })
            observableEmployeeListRefreshEvent.observe(viewLifecycleOwner, Observer {
                if (it == null) return@Observer
                updateSwipeRefreshProgress(it.state)
            })
        }
    }

    override fun onRefreshRequested(forced: Boolean) {
        viewModel.getEmployeeList(limit = Random(50).nextInt(100, 200))
    }

    companion object {
        fun newInstance(): TableUIFragment = TableUIFragment()
    }
}

class TableUIFragmentFactory : FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment =
        TableUIFragment.newInstance()
}