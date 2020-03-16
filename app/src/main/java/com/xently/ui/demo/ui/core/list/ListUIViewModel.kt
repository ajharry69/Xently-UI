package com.xently.ui.demo.ui.core.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.xently.ui.demo.viewmodels.EmployeeViewModel

class ListUIViewModel : EmployeeViewModel()

@Suppress("UNCHECKED_CAST")
class ListUIViewModelFactory : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = ListUIViewModel() as T
}
