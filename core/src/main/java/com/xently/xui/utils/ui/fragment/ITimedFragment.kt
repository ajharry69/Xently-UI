package com.xently.xui.utils.ui.fragment

import android.view.View
import android.widget.TextView
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeUnit.MILLISECONDS

/**
 * Count down for 10000ms or 10s
 */
const val DEFAULT_COUNTDOWN_TIMEOUT_MILLIS: Long = 10000

/**
 * Count down for... (25 + ([DEFAULT_COUNTDOWN_TIMEOUT_MILLIS] / 1000))ms
 */
const val DEFAULT_TOTAL_COUNTDOWN_TIMEOUT_MILLIS: Long =
    (25 * 1000) + DEFAULT_COUNTDOWN_TIMEOUT_MILLIS

/**
 * Count down after every 1000ms / 1s
 */
const val DEFAULT_COUNTDOWN_AFTER_EVERY_MILLIS: Long = 1000

interface IBaseTimedFragment {

    /**
     * what to do when count down is active (ticking)
     */
    fun onTickActive(
        millisUntilFinish: Long,
        counter: TextView,
        container: View? = null,
        showTimer: Pair<Long, TimeUnit> = Pair(DEFAULT_COUNTDOWN_TIMEOUT_MILLIS, MILLISECONDS),
        updateTimerEvery: Pair<Long, TimeUnit> = Pair(
            DEFAULT_COUNTDOWN_AFTER_EVERY_MILLIS,
            MILLISECONDS
        ),
        showCounter: Boolean = true
    )

    /**
     * what to do when count down is finished
     */
    fun onTickFinished(counter: TextView, container: View? = null)

    fun onResendButtonClick()
}