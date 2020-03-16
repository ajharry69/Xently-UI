package com.xently.ui.demo.ui.core.list.filter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.xently.ui.demo.ui.core.EmployeeListFragment

class FilteredListFragment : EmployeeListFragment() {

    private val args: FilteredListFragmentArgs by navArgs()

    override val viewModel: FilteredListViewModel by viewModels {
        FilteredListViewModelFactory()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.getObservableEmployeeList(args.searchQuery).observe(viewLifecycleOwner, Observer {
            onObservableListChanged(it)

            listAdapter.submitList(it)
        })
    }

    companion object {
        fun newInstance(): FilteredListFragment = FilteredListFragment()
    }
}

class FilteredListFragmentFactory : FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment =
        FilteredListFragment.newInstance()
}
