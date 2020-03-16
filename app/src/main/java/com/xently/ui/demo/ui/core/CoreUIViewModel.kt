package com.xently.ui.demo.ui.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CoreUIViewModel : ViewModel()

@Suppress("UNCHECKED_CAST")
class CoreUIViewModelFactory : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = CoreUIViewModel() as T
}
