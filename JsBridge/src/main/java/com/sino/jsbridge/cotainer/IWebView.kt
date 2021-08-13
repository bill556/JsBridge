package com.sino.jsbridge.cotainer

import android.content.Context
import android.webkit.ValueCallback


interface IWebView {

    fun execJs(
        methodName: String,
        params: String? = null,
        valueCallback: ValueCallback<String>? = null
    )

    fun execJs(sourceJs: String, valueCallback: ValueCallback<String>? = null)

    fun runOnMainThread(runnable: Runnable)

    fun getCurUrl(): String

    fun getCurContext(): Context

    fun getCurWebHelper(): WebHelper
}