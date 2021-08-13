package com.sino.jsbridge.handler

import android.widget.Toast
import com.sino.jsbridge.bridge.JsCallBacker
import com.sino.jsbridge.cotainer.IWebViewContainer
import org.json.JSONObject

class CommonJsHandler : BaseJsApiHandler() {

    override fun handleApi(apiName: String, params: String, jsCallBacker: JsCallBacker): Boolean {
        when (apiName) {
            "closeWindow" -> {
                getContainerOp<IWebViewContainer>()?.closeWindow()
                return true
            }
            "setTitle" -> {
                getContainerOp<IWebViewContainer>()?.updateTitle(JSONObject(params).getString("title"))
                jsCallBacker.success()
                return true
            }
            "config" -> {
                Toast.makeText(getActivity(), "config", Toast.LENGTH_SHORT).show()
                return true
            }
        }
        return false
    }
}