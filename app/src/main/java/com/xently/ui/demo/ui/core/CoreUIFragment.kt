package com.xently.ui.demo.ui.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation.createNavigateOnClickListener
import com.xently.ui.demo.databinding.CoreUiFragmentBinding
import com.xently.ui.demo.ui.core.CoreUIFragmentDirections.Companion.coreListUi
import com.xently.ui.demo.ui.core.CoreUIFragmentDirections.Companion.coreTableUi
import com.xently.xui.Fragment

class CoreUIFragment : Fragment() {

    private var _binding: CoreUiFragmentBinding? = null
    private val binding: CoreUiFragmentBinding
        get() = _binding!!

    private val viewModel: CoreUIViewModel by viewModels {
        CoreUIViewModelFactory()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CoreUiFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.coreListUi.setOnClickListener(createNavigateOnClickListener(coreListUi()))
        binding.coreTableUi.setOnClickListener(createNavigateOnClickListener(coreTableUi()))
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        fun newInstance(): CoreUIFragment = CoreUIFragment()
    }
}

class CoreUIFragmentFactory : FragmentFactory() {
    override fun instantiate(
        classLoader: ClassLoader,
        className: String
    ): androidx.fragment.app.Fragment = CoreUIFragment.newInstance()
}
