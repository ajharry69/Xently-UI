package com.xently.xui.adapters.viewpager

fun middleFragmentPosition(tabCount: Int): Int {
    return if (tabCount <= 0 || tabCount == 1) {
        0
    } else {
        val fl = tabCount.toFloat() / 2
        when {
            tabCount % 2 == 0 -> fl.toInt() - 1
            else -> fl.toInt()
        }
    }
}