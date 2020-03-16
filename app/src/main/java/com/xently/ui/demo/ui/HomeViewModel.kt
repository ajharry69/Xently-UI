package com.xently.ui.demo.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class HomeViewModel : ViewModel() {
    // TODO: Implement the ViewModel
}

@Suppress("UNCHECKED_CAST")
class HomeViewModelFactory : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = HomeViewModel() as T
}
