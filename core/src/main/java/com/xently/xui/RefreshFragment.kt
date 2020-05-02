package com.xently.xui

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.xently.xui.utils.IRefresh
import com.xently.xui.utils.ListLoadEvent
import com.xently.xui.utils.ListLoadEvent.Status.*
import com.xently.xui.utils.RefreshEvent
import com.xently.xui.utils.RefreshEvent.State.*
import com.xently.xui.utils.ui.view.hideViews
import com.xently.xui.utils.ui.view.showProgress
import com.xently.xui.utils.ui.view.showViews

abstract class RefreshFragment<T> : Fragment(), IRefresh {
    private val subClassName = this::class.java.name
    protected var onRefreshListener: OnRefreshListener? = null

    protected var swipeRefresh: SwipeRefreshLayout? = null
    protected lateinit var statusContainer: View

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onRefreshListener = OnRefreshListener {
            onRefreshEvent(RefreshEvent(STARTED, true))
        }
        swipeRefresh?.setOnRefreshListener(onRefreshListener)
    }

    override fun onPause() {
        onRefreshEvent()
        super.onPause()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.menu_list_refresh -> {
            onRefreshListener?.onRefresh()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    open fun onRefreshEvent(event: RefreshEvent = RefreshEvent(ENDED)) {
        when (event.state) {
            STARTED -> {
                swipeRefresh?.showProgress()
                onRefreshRequested(event.forced)
            }
            ACTIVE -> swipeRefresh?.showProgress()
            ENDED -> swipeRefresh?.showProgress(false)
        }
    }

    /**
     * @see onRefreshEvent
     */
    protected open fun onListLoadEvent(event: ListLoadEvent<T>) {
        when (event.status) {
            NULL -> {
                onRefreshEvent(RefreshEvent(STARTED))
                hideViews(statusContainer)
                showViews(swipeRefresh)
            }
            EMPTY -> {
                onRefreshEvent()
                hideViews(swipeRefresh)
                showViews(statusContainer)
            }
            LOADED -> {
                onRefreshEvent()
                hideViews(statusContainer)
                showViews(swipeRefresh)
            }
        }
    }

    /**
     * Should be called in a list-returning observable e.g. LiveData
     */
    protected fun <L : List<T>> onObservableListChanged(list: L?, forceShow: Boolean = false) {
        val event = if (list.isNullOrEmpty()) {
            if (forceShow) ListLoadEvent(LOADED, list) else {
                if (list == null) ListLoadEvent(NULL, list)
                else ListLoadEvent(EMPTY, list)
            }
        } else ListLoadEvent(LOADED, list)

        onListLoadEvent(event)
    }
}