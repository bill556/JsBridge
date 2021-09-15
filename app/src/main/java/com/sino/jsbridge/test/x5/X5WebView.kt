package com.sino.jsbridge.test.x5

import android.content.Context
import android.os.Build
import android.os.Looper
import android.util.AttributeSet
import android.webkit.ValueCallback
import com.sino.jsbridge.SinoJsBridge
import com.sino.jsbridge.bridge.JavascriptInterface
import com.sino.jsbridge.cotainer.IWebView
import com.sino.jsbridge.cotainer.WebHelper
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient


class X5WebView : WebView, IWebView {

    constructor(p0: Context?) : super(p0)
    constructor(p0: Context?, p1: AttributeSet?) : super(p0, p1)
    constructor(p0: Context?, p1: AttributeSet?, p2: Int) : super(p0, p1, p2)

    private val webHelper: WebHelper by lazy { WebHelper(this) }

    init {
        val settings = settings

        settings.javaScriptEnabled = true

        settings.mixedContentMode = 0
        settings.domStorageEnabled = true;// 打开本地缓存提供JS调用,至关重要
        settings.allowFileAccess = true;
        settings.setAppCacheEnabled(true);
        settings.databaseEnabled = true;
        settings.userAgentString = settings.userAgentString + " sinojs/1.0.0"
        settings.allowFileAccess = true
        settings.setAllowUniversalAccessFromFileURLs(true)
        settings.setAllowFileAccessFromFileURLs(true)

        //提供js-bridge层注入
        webViewClient = InnerCustomWebViewClient()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setWebContentsDebuggingEnabled(true)
        }

        //注册bridge事件处理对象
        addJavascriptInterface(JavascriptInterface(this), JavascriptInterface.INTERFACE_NAME)
    }

    override fun getCurUrl(): String {
        return url
    }

    override fun getCurContext(): Context {
        return context
    }

    override fun getCurWebHelper(): WebHelper {
        return webHelper
    }

    override fun execJs(
        methodName: String,
        params: String?,
        valueCallback: ValueCallback<String>?
    ) {
        val js: String = if (params.isNullOrBlank()) {
            String.format("%s()", methodName)
        } else {
            String.format("%s('%s')", methodName, params)
        }
        execJs(js, valueCallback)
    }

    override fun execJs(sourceJs: String, valueCallback: ValueCallback<String>?) {
        if (SinoJsBridge.JS_DEBUG) SinoJsBridge.log("evaluateJavascript:javascript:$sourceJs")
        runOnMainThread(Runnable {
            evaluateJavascript("javascript:$sourceJs") { valueCallback?.onReceiveValue(it) }
        })
    }

    override fun runOnMainThread(runnable: Runnable) {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            runnable.run()
            return
        }
        post(runnable)
    }

    private inner class InnerCustomWebViewClient : WebViewClient() {
        override fun onPageFinished(webView: WebView?, s: String?) {
            super.onPageFinished(webView, s)
            //注入js-bridge，可以多次调用，js-bridge有幂等处理
            webHelper.injectCoreJs()
        }

        override fun doUpdateVisitedHistory(p0: WebView?, p1: String?, p2: Boolean) {
            super.doUpdateVisitedHistory(p0, p1, p2)
            //注入js-bridge，可以多次调用，js-bridge有幂等处理
            webHelper.injectCoreJs()
        }

        //web页面加载资源时都会经过此方法，可以拦截特定url，返回处理过的WebResourceResponse
        //override fun shouldInterceptRequest(p0: WebView?, p1: String?): WebResourceResponse {
//            if (p1!!.startsWith("sfile://")) {
//                return WebResourceResponse("image/png", "", 200, "ok", null, FileInputStream(Environment.getExternalStorageDirectory().path + File.separator + "123.png"))
//            }
//            if (p1!!.startsWith("content://")) {
//                Log.e("shouldInterceptRequest", p1)
//                return WebResourceResponse("image/png", "", 200, "ok", null, FileInputStream(File(URI.create(p1))))
//            }
        //return super.shouldInterceptRequest(p0, p1)
        // }
    }
}
