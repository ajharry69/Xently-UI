package com.xently.xui.utils

interface IRefresh {

    /**
     * Refresh or fetch data based on [forced]
     * @param forced indicates a data refresh/fetch is mandatory i.e. any conditional checks
     * (e.g. check if number of cached records match last reported number from the server) to limit
     * refresh/fetch rates should be by-passed and a network fetch should be started either way
     */
    fun onRefreshRequested(forced: Boolean = true) = Unit
}