package com.sino.jsbridge

import android.util.Log

class SinoJsBridge {
    companion object {
        var JS_DEBUG = false
        var logFun: ((msg: String?) -> Unit)? = null

        fun init(debug: Boolean, log: ((msg: String?) -> Unit)? = null) {
            JS_DEBUG = debug
            logFun = log
        }

        fun log(msg: String?) {
            if (JS_DEBUG) {
                val logFun = logFun
                if (logFun != null) {
                    logFun(msg)
                    return
                }
                Log.d("SinoJsBridge", msg)
            }
        }
    }
}