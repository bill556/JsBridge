package com.sino.jsbridge.test.test

import android.widget.Toast
import com.sino.jsbridge.bridge.JsCallBacker
import com.sino.jsbridge.handler.BaseJsApiHandler
import org.json.JSONObject

class TestJsHandler : BaseJsApiHandler() {

    override fun handleApi(apiName: String, params: String, jsCallBacker: JsCallBacker): Boolean {
        when (apiName) {
            "_test_unicode_params_and_result" -> {
                val activity = getActivity() ?: return true

                Toast.makeText(activity, params, Toast.LENGTH_LONG).show()

                val result = JSONObject()
                result.put("unicodeResult", "中文 Emoji 😂🤣")
                jsCallBacker.success(result)
                return true
            }
        }
        return false
    }
}