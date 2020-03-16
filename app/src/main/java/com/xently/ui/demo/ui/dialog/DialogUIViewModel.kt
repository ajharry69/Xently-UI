package com.xently.ui.demo.ui.dialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class DialogUIViewModel : ViewModel() {
    // TODO: Implement the ViewModel
}

@Suppress("UNCHECKED_CAST")
class DialogUIViewModelFactory : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = DialogUIViewModel() as T
}
