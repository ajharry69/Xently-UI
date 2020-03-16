package com.xently.ui.demo.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation.createNavigateOnClickListener
import com.xently.ui.demo.databinding.HomeFragmentBinding
import com.xently.ui.demo.ui.HomeFragmentDirections.Companion.coreUi
import com.xently.ui.demo.ui.HomeFragmentDirections.Companion.dialogUi
import com.xently.xui.Fragment

class HomeFragment : Fragment() {

    private var _binding: HomeFragmentBinding? = null
    private val binding: HomeFragmentBinding
        get() = _binding!!

    private val viewModel: HomeViewModel by viewModels {
        HomeViewModelFactory()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = HomeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.coreUi.setOnClickListener(createNavigateOnClickListener(coreUi()))
        binding.dialogUi.setOnClickListener(createNavigateOnClickListener(dialogUi()))
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        fun newInstance() = HomeFragment()
    }
}

class HomeFragmentFactory : FragmentFactory() {
    override fun instantiate(
        classLoader: ClassLoader,
        className: String
    ): androidx.fragment.app.Fragment = HomeFragment.newInstance()
}
