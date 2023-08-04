package com.humanverificationdemo

import android.app.Application
import com.humanverificationdemo.utils.SharedPrefsUtils

class MediaPipeDemoApp : Application() {

    override fun onCreate() {
        super.onCreate()
        SharedPrefsUtils.init(this)
    }
}