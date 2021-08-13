package com.sino.jsbridge.cotainer

import android.content.Intent
import android.webkit.ValueCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.sino.jsbridge.SinoJsBridge
import com.sino.jsbridge.bridge.JsCallBacker
import com.sino.jsbridge.bridge.JsEventer
import com.sino.jsbridge.handler.IJsApiHandler
import java.io.InputStreamReader
import java.util.*

/**
 * 处理一些预置行为
 * 例如注入sinojs.js到当前页面，此过程会协商加密密钥
 *
 * @property iWebView IZWebView
 * @property dgtVerifyRandomStr String 与bridge约定的加密密钥
 * @property apiHandlers MutableSet<IJsApiHandler> 处理web的api请求对象，由容器初始化
 * @constructor
 */
class WebHelper(private val iWebView: IWebView) {

    val dgtVerifyRandomStr: String by lazy { UUID.randomUUID().toString() }

    //  加载js处理
    private val injectCoreJsStr: String by lazy {
        val readLines =
            InputStreamReader(iWebView.getCurContext().assets.open("jsapi/sinojs.js")).readLines()
        val js = StringBuilder()
        readLines.fold(js, { acc, s ->
            acc.append(
                s.replace("\${_dgtVerifyRandomStr}", dgtVerifyRandomStr)
                    .replace("\${_debugLevel}", if (SinoJsBridge.JS_DEBUG) "0" else "2")
            ).append("\n")
        })
        js.toString()
    }

    fun injectCoreJs() {
        iWebView.execJs(injectCoreJsStr,
            ValueCallback {
                if (SinoJsBridge.JS_DEBUG) SinoJsBridge.log("url: ${iWebView.getCurUrl()}\ninject result: $it")
            })
    }

    private val apiHandlers: MutableSet<IJsApiHandler> by lazy { hashSetOf<IJsApiHandler>() }

    fun dispatchExeApi(apiName: String, params: String, jsCallBacker: JsCallBacker) {
        iWebView.runOnMainThread(Runnable {
            try {
                for (handler in apiHandlers) {
                    if (handler.handleApi(apiName, params, jsCallBacker)) {
                        return@Runnable
                    }
                }
            } catch (e: Exception) {
                jsCallBacker.fail(JsCallBacker.CODE_ERR_FAIL, e.toString())
                return@Runnable
            }
            jsCallBacker.fail(JsCallBacker.CODE_ERR_404, "")
        })
    }

    fun dispatchContainerResult(requestCode: Int, resultCode: Int, data: Intent?) {
        for (handler in apiHandlers) {
            if (handler.onContainerResult(requestCode, resultCode, data)) {
                return
            }
        }
    }

    fun dispatchContainerDestroy() {
        for (handler in apiHandlers) {
            handler.onContainerDestroy()
        }
    }

    fun registeredJsApiHandler(fragment: Fragment, clazz: Class<out IJsApiHandler>) {
        val jsApiHandler = clazz.newInstance()
        jsApiHandler.onAttachContainer(fragment)
        apiHandlers.add(jsApiHandler)
    }

    fun registeredJsApiHandler(fragment: Fragment, jsApiHandler: IJsApiHandler) {
        jsApiHandler.onAttachContainer(fragment)
        apiHandlers.add(jsApiHandler)
    }

    fun registeredJsApiHandler(
        fragmentActivity: FragmentActivity,
        clazz: Class<out IJsApiHandler>
    ) {
        val jsApiHandler = clazz.newInstance()
        jsApiHandler.onAttachContainer(fragmentActivity)
        apiHandlers.add(jsApiHandler)
    }

    fun registeredJsApiHandler(fragmentActivity: FragmentActivity, jsApiHandler: IJsApiHandler) {
        jsApiHandler.onAttachContainer(fragmentActivity)
        apiHandlers.add(jsApiHandler)
    }

    val jsEventer: JsEventer by lazy { JsEventer(iWebView) }
}