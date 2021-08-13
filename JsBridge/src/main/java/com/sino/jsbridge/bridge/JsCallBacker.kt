package com.sino.jsbridge.bridge

import com.sino.jsbridge.SinoJsBridge
import com.sino.jsbridge.cotainer.IWebView
import com.sino.jsbridge.util.Utils
import org.json.JSONArray
import org.json.JSONObject
import java.lang.ref.WeakReference


class JsCallBacker(
    private val bridgeMessage: BridgeMessage,
    private val iWebView: WeakReference<IWebView>
) {

    companion object {
        const val CODE_SUCCESS = 0   //成功
        const val CODE_ERR_CANCEL = 1  //取消操作
        const val CODE_ERR_FAIL = 3 //未知错误
        const val CODE_ERR_INVALID = 400  //无效的请求参数
        const val CODE_ERR_FORBIDDEN = 403 //没有该方法的调用权限
        const val CODE_ERR_404 = 404  //请求的方法或者事件名没有找到
    }

    fun cancel() {
        val jsonObject = JSONObject()
        jsonObject.put("errCode", CODE_ERR_CANCEL)
        doHandle(jsonObject)
    }

    fun fail(errCode: Int = CODE_ERR_FAIL, errMsg: String = "") {
        val jsonObject = JSONObject()
        jsonObject.put("errCode", errCode)
        jsonObject.put("errMsg", errMsg)
        doHandle(resultObj = jsonObject)
    }

    fun success(jsonObject: JSONObject = JSONObject()) {
        jsonObject.put("errCode", CODE_SUCCESS)
        doHandle(resultObj = jsonObject)
    }

    fun success(key: String = "result", jsonArray: JSONArray) {
        val jsonObject = JSONObject()
        jsonObject.put("errCode", CODE_SUCCESS)
        jsonObject.put(key, jsonArray)
        doHandle(resultObj = jsonObject)
    }

    private fun doHandle(resultObj: JSONObject) {
        iWebView.get()?.let { izWebView ->
            try {
                val jsonMessage = JSONObject()

                jsonMessage.put("msgType", "callback")
                jsonMessage.put("callbackId", bridgeMessage.callbackId)
                jsonMessage.put("params", resultObj)

                val jsonMessageBase64 = Utils.base64Encode(jsonMessage.toString())

                val shaKey =
                    Utils.signatureSHA1("$jsonMessageBase64${izWebView.getCurWebHelper().dgtVerifyRandomStr}")

                val toBridgeRet = JSONObject()
                toBridgeRet.put("jsonMessage", jsonMessageBase64)
                toBridgeRet.put("shaKey", shaKey)

                izWebView.execJs("sinoJSBridge._handleMessageFromNative", toBridgeRet.toString(), null)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } ?: if (SinoJsBridge.JS_DEBUG) SinoJsBridge.log("$bridgeMessage izWebView recycle")
    }
}