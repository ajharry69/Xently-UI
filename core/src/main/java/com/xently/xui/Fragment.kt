package com.xently.xui

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.annotation.CallSuper
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import com.xently.xui.utils.Log
import com.xently.xui.utils.ui.IModifyToolbar
import com.xently.xui.utils.ui.fragment.IFragment

open class Fragment : androidx.fragment.app.Fragment(), IFragment {

    private val backPressDispatcher by lazy { requireActivity().onBackPressedDispatcher }

    private var backPressCallback: OnBackPressedCallback? = null

    private var iModifyToolbar: IModifyToolbar? = null

    /**
     * What to set as title of [Toolbar]
     * @see IModifyToolbar
     */
    open val toolbarTitle: String? get() = null

    /**
     * What to set as title of [Toolbar]
     * @see IModifyToolbar
     */
    open val toolbarSubTitle: String? get() = null

    /**
     * Option to hide/show [Toolbar]. Default behaviour is **true** (show)
     * @see IModifyToolbar
     */
    open val showToolbar: Boolean get() = true

    open val toolbarUpIcon: Int?
        @DrawableRes get() = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        backPressCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() = onBackPressed()
        }
        backPressDispatcher.addCallback(this, backPressCallback!!)

        setHasOptionsMenu(true)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is IModifyToolbar) iModifyToolbar = context
    }

    override fun onDetach() {
        iModifyToolbar = null
        super.onDetach()
    }

    override fun onResume() {
        super.onResume()
        iModifyToolbar?.onModifyToolbar(
            toolbarTitle,
            toolbarSubTitle,
            toolbarUpIcon,
            showToolbar
        )
    }

    @CallSuper
    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        item.onNavDestinationSelected(findNavController()) || super.onOptionsItemSelected(item)

    /**
     * What to do when system/phone's **_back-button_** is pressed
     */
    open fun onBackPressed() {
        Log.show("BaseFragment", "onBackPressed: ${this::class.java.name}")
        backPressCallback?.isEnabled = false
        backPressDispatcher.onBackPressed()
    }
}