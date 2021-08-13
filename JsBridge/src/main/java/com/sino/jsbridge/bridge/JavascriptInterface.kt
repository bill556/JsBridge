package com.sino.jsbridge.bridge

import android.webkit.JavascriptInterface
import com.sino.jsbridge.SinoJsBridge
import com.sino.jsbridge.cotainer.IWebView
import java.lang.ref.WeakReference

class JavascriptInterface(private val qWebView: IWebView) {

    companion object {
        const val INTERFACE_NAME = "__sino"
    }

    //bridge层请求native api 参考readme-protocol.md
    @JavascriptInterface
    fun _sendMessage(msg: String?) {
        if (SinoJsBridge.JS_DEBUG) SinoJsBridge.log("bridge _sendMessage:$msg")

        if (msg != null) {
            try {
                //参考readme-protocol.md数据接口，解析成BridgeMessage对象
                val bridgeMessage =
                    BridgeMessage.parse7Check(msg, qWebView.getCurWebHelper().dgtVerifyRandomStr)

                val handler = JsCallBacker(bridgeMessage, iWebView = WeakReference(qWebView))

                qWebView.getCurWebHelper().dispatchExeApi(bridgeMessage.apiName, bridgeMessage.params, handler)
            } catch (e: Exception) {
                if (SinoJsBridge.JS_DEBUG) SinoJsBridge.log("_sendMessage  e:$e")
            }
        }
    }
}
