package com.xently.xui.utils.ui.fragment

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

interface ISwipeRefreshFragment {

    /**
     * Refresh or fetch data based on [forced]
     * @param forced indicates a data refresh/fetch is mandatory i.e. any conditional checks
     * (e.g. check if number of cached records match last reported number from the server) to limit
     * refresh/fetch rates should be by-passed and a network fetch should be started either way
     */
    fun onRefreshRequested(forced: Boolean = true) = Unit

    fun SwipeRefreshLayout.showProgress(show: Boolean = true) {
        // Start the refresh process just in case it wasn't
        if (!isRefreshing && show) isRefreshing = true

        // End the refresh process just in case it wasn't
        if (isRefreshing && !show) isRefreshing = false
    }
}