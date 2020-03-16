package com.xently.ui.demo.ui.core.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.xently.ui.demo.ui.core.EmployeeListFragment

class ListUIFragment : EmployeeListFragment() {

    private val args: ListUIFragmentArgs by navArgs()

    override val viewModel: ListUIViewModel by viewModels {
        ListUIViewModelFactory()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.getObservableEmployeeList(args.searchQuery).observe(viewLifecycleOwner, Observer {
            onObservableListChanged(it)

            listAdapter.submitList(it)
        })
    }

    companion object {
        fun newInstance(): ListUIFragment = ListUIFragment()
    }
}

class ListUIFragmentFactory : FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment =
        ListUIFragment.newInstance()
}
