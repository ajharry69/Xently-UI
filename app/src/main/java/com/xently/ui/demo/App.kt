package com.xently.ui.demo

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        if (!AppCompatDelegate.isCompatVectorFromResourcesEnabled()) {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }
}