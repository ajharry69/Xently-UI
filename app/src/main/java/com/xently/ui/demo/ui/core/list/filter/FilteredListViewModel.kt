package com.xently.ui.demo.ui.core.list.filter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.xently.ui.demo.viewmodels.EmployeeViewModel

class FilteredListViewModel : EmployeeViewModel()

@Suppress("UNCHECKED_CAST")
class FilteredListViewModelFactory : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = FilteredListViewModel() as T
}
