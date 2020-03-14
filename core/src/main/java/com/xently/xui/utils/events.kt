package com.xently.xui.utils

/**
 * Monitors and responds to list refresh/fetch(to some extend) based on [state] value
 * @param state refresh state can either be [State.STARTED], [State.ACTIVE] or [State.ENDED]
 * @param forced indicates a list refresh is mandatory i.e. any conditional checks(e.g. check if
 * number of cached records match last reported number from the server) to limit refresh/fetch rates
 * should be by-passed and a network fetch should be started either way
 */
data class RefreshEvent(val state: State, val forced: Boolean = false) {
    enum class State {
        /**
         * refresh/fetch started
         */
        STARTED,

        /**
         * refresh/fetch is progress
         */
        ACTIVE,

        /**
         * refresh/fetch ended
         */
        ENDED
    }
}

/**
 * Monitors and responds to list load status based on [status] value
 * @param status list load status, can either be [Status.NULL], [Status.EMPTY] or [Status.LOADED]
 * @param data loaded list iff [status] equal [Status.LOADED]
 */
data class ListLoadEvent<T>(val status: Status, val data: List<T>? = null) {
    enum class Status {
        /**
         * list - null. Most so when dealing with observables e.g. LiveData
         */
        NULL,

        /**
         * list - 0(Zero)-sized
         */
        EMPTY,

        /**
         * list - contains at least one item of [T] and [data] should contain the same iff it was
         * initialized when status was reported
         */
        LOADED
    }
}