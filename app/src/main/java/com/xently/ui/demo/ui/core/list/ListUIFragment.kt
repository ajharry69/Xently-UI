package com.xently.ui.demo.ui.core.list

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.xently.ui.demo.R
import com.xently.ui.demo.ui.core.EmployeeListFragment

/**
 * Demonstrates how to:
 *
 *  1. inflate a custom menu on top of the existing
 *  2. show and modify the floating-action-button shown at the bottom-right of the screen
 *  3. initiate a search from this fragment, passing the search query to the search results showing
 *  fragment through a Searchable activity
 */
class ListUIFragment : EmployeeListFragment() {

    private val args: ListUIFragmentArgs by navArgs()

    override val viewModel: ListUIViewModel by viewModels {
        ListUIViewModelFactory()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeEmployeeList(args.searchQuery)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_sort_filter, menu)
    }

    companion object {
        fun newInstance(): ListUIFragment = ListUIFragment()
    }
}

class ListUIFragmentFactory : FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment =
        ListUIFragment.newInstance()
}
