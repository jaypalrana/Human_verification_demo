package com.humanverificationdemo.utils

import android.util.Log
import com.humanverificationdemo.BuildConfig

class Logger {
    companion object {
        @JvmStatic
        fun v(tag: String, msg: String) {
            if (BuildConfig.DEBUG)
                Log.v(tag, msg)
        }

        @JvmStatic
        fun v(msg: String) {
            if (BuildConfig.DEBUG)
                Log.v("Test->", msg)
        }
    }
}