package com.xently.xui

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.xently.xui.utils.ListLoadEvent
import com.xently.xui.utils.ListLoadEvent.Status.*
import com.xently.xui.utils.RefreshEvent
import com.xently.xui.utils.RefreshEvent.State.*
import com.xently.xui.utils.ui.fragment.ISwipeRefreshFragment

abstract class SwipeRefreshFragment<T> : Fragment(), ISwipeRefreshFragment {
    private val subClassName = this::class.java.name
    protected var onRefreshListener: SwipeRefreshLayout.OnRefreshListener? = null

    protected lateinit var swipeRefresh: SwipeRefreshLayout
    protected lateinit var statusContainer: View

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
            onRefreshEvent(RefreshEvent(STARTED, true))
        }
        swipeRefresh.setOnRefreshListener(onRefreshListener)
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
                swipeRefresh.showProgress()
                onRefreshRequested(event.forced)
            }
            ACTIVE -> swipeRefresh.showProgress()
            ENDED -> swipeRefresh.showProgress(false)
        }
    }

    /**
     * @see onRefreshEvent
     */
    protected open fun onListLoadEvent(event: ListLoadEvent<T>) {
        when (event.status) {
            NULL -> {
                onRefreshEvent(RefreshEvent(STARTED))
                hideViewsCompletely(statusContainer)
                showViews(swipeRefresh)
            }
            EMPTY -> {
                onRefreshEvent()
                hideViewsCompletely(swipeRefresh)
                showViews(statusContainer)
            }
            LOADED -> {
                onRefreshEvent()
                hideViewsCompletely(statusContainer)
                showViews(swipeRefresh)
            }
        }
    }

    /**
     * Should be called in a list-returning observable e.g. LiveData
     */
    protected fun <L : List<T>> onObservableListChanged(list: L?) {
        val event = if (list.isNullOrEmpty()) {
            if (list == null) ListLoadEvent(NULL, list)
            else ListLoadEvent(EMPTY, list)
        } else ListLoadEvent(LOADED, list)

        onListLoadEvent(event)
    }
}