package com.xently.xui

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.xently.xui.utils.ListLoadEvent
import com.xently.xui.utils.ListLoadEvent.Status.*
import com.xently.xui.utils.Log
import com.xently.xui.utils.RefreshEvent
import com.xently.xui.utils.RefreshEvent.State
import com.xently.xui.utils.RefreshEvent.State.*
import com.xently.xui.utils.ui.fragment.ISwipeRefreshFragment
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

abstract class SwipeRefreshFragment<T> : Fragment(), ISwipeRefreshFragment {
    private val subClassName = this::class.java.name
    private var eventBusRegistered: Boolean = false
    protected var onRefreshListener: SwipeRefreshLayout.OnRefreshListener? = null

    protected lateinit var swipeRefresh: SwipeRefreshLayout
    protected lateinit var statusContainer: View

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
            if (eventBusRegistered) {
                eventBus.post(RefreshEvent(STARTED, true))
            } else onRefreshRequested(true)
        }
        swipeRefresh.setOnRefreshListener(onRefreshListener)
    }

    override fun onResume() {
        super.onResume()
        /*
        EventBus post mechanism is also used to start network request(s) hence the registration must
        must be done here to PREVENT unnecessary kick off off a network request when a page is not
        visible e.g. in TAB or ViewPager fragment situations
         */
        eventBusRegistered = eventBus.isRegistered(this)
        if (!eventBusRegistered) {
            eventBus.register(this)
            eventBusRegistered = eventBus.isRegistered(this)
        }
        logEventBusRegistrationStatus("onResume")
    }

    override fun onPause() {
        swipeRefresh.showProgress(false)
        eventBusRegistered = eventBus.isRegistered(this)
        if (eventBusRegistered) {
            eventBus.unregister(this)
            eventBusRegistered = eventBus.isRegistered(this)
        }
        logEventBusRegistrationStatus("onPause")
        super.onPause()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.menu_list_refresh -> {
            onRefreshListener?.onRefresh()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onRefreshEvent(event: RefreshEvent) {
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
    @Subscribe(threadMode = ThreadMode.MAIN)
    protected open fun onListLoadEvent(event: ListLoadEvent<T>) {
        when (event.status) {
            NULL -> {
                updateSwipeRefreshProgress()
                hideViewsCompletely(statusContainer)
                showViews(swipeRefresh)
            }
            EMPTY -> {
                updateSwipeRefreshProgress(ENDED)
                hideViewsCompletely(swipeRefresh)
                showViews(statusContainer)
            }
            LOADED -> {
                updateSwipeRefreshProgress(ENDED)
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

        if (eventBusRegistered) eventBus.post(event) else onListLoadEvent(event)
    }

    /**
     * Can be called when a list refresh is ended with [state] = [State.ENDED] to hide the swipe
     * refresh layout's progress indicator
     */
    protected fun updateSwipeRefreshProgress(state: State = ACTIVE) {
        if (eventBusRegistered) {
            eventBus.post(RefreshEvent(state))
        } else swipeRefresh.showProgress(state != ENDED)
    }

    private fun logEventBusRegistrationStatus(sep: String) {
        Log.show(LOG_TAG, "<<${subClassName}>> $sep EventBus Registered: $eventBusRegistered")
    }

    companion object {
        private val LOG_TAG = SwipeRefreshFragment::class.java.simpleName
    }
}