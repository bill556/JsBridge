package com.sino.jsbridge.bridge

import com.sino.jsbridge.SinoJsBridge
import com.sino.jsbridge.util.Utils
import org.json.JSONObject

data class BridgeMessage(
    var apiName: String,
    var params: String,
    var callbackId: String,
    var msgType: String
) {
    companion object {
        /**
         * 解析sinojs层请求过来的数据
         * 保证必要数据都是符合要求的，数据结构详见 [readme-protocol.md]
         * @param msg String 请求元数据
         * @param dgtVerifyRandomStr String 加密密钥
         * @return BridgeMessage
         */
        fun parse7Check(msg: String, dgtVerifyRandomStr: String): BridgeMessage {
            try {
                val msgJson = JSONObject(msg)
                val jsonMessage = msgJson.optString("jsonMessage")
                // sha1(jsonMessage+_dgtVerifyRandomStr)
                val shaKey = msgJson.optString("shaKey")

                //检验数据签名不一致
                require(Utils.signatureSHA1("${jsonMessage}$dgtVerifyRandomStr") == shaKey) { "JsSendMessage parse7Check error shaKey" }

                val jsonMessageJson = JSONObject(jsonMessage)

                //require，请求的api name
                val apiName = jsonMessageJson.optString("apiName")
                requireNotNull(apiName) { "error apiName" }

                //require，当前请求sinojs层回调id，用于执行完成后回调
                val callbackId = jsonMessageJson.optString("callbackId")
                requireNotNull(callbackId) { "error callbackId" }

                //可能是 ""，当前api请求携带数据
                val params = jsonMessageJson.optString("params", "")
                requireNotNull(params) { "error params" }

                //请求类型，预置扩展。目前一直是 "call"
                val msgType = jsonMessageJson.optString("msgType", "call")
                requireNotNull(msgType) { "error msgType" }

                return BridgeMessage(apiName, params, callbackId, msgType)
            } catch (e: Exception) {
                if (SinoJsBridge.JS_DEBUG) SinoJsBridge.log("JsSendMessage parse7Check null $msg \ndgtVerifyRandomStr: $dgtVerifyRandomStr \n e:$e")
                throw e
            }
        }
    }
}