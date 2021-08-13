package com.sino.jsbridge.bridge

import android.webkit.ValueCallback
import com.sino.jsbridge.cotainer.IWebView
import com.sino.jsbridge.util.Utils
import org.json.JSONArray
import org.json.JSONObject

class JsEventer(private val iWebView: IWebView) {

    fun event(
        eventName: String,
        jsonObject: JSONObject = JSONObject(),
        valueCallback: ValueCallback<String>? = null
    ) {
        doHandle(eventName, jsonObject, valueCallback)
    }

    fun event(
        eventName: String,
        key: String = "result",
        jsonArray: JSONArray = JSONArray(),
        valueCallback: ValueCallback<String>? = null
    ) {
        val jsonObject = JSONObject()
        jsonObject.put(key, jsonArray)

        doHandle(eventName, jsonObject, valueCallback)
    }

    private fun doHandle(
        eventName: String,
        jsonObject: JSONObject,
        valueCallback: ValueCallback<String>? = null
    ) {
        val jsonMessage = JSONObject()
        jsonMessage.put("eventName", eventName)
        jsonMessage.put("msgType", "event")
        jsonMessage.put("params", jsonObject)

        val jsonMessageBase64 = Utils.base64Encode(jsonMessage.toString())

        val shaKey =
            Utils.signatureSHA1("$jsonMessageBase64${iWebView.getCurWebHelper().dgtVerifyRandomStr}")

        val toBridgeRet = JSONObject()
        toBridgeRet.put("jsonMessage", jsonMessageBase64)
        toBridgeRet.put("shaKey", shaKey)

        iWebView.execJs("sinoJSBridge._handleMessageFromNative", toBridgeRet.toString(), valueCallback)
    }
}