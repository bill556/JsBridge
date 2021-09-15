package com.sino.jsbridge.test.x5

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sino.jsbridge.cotainer.IWebViewContainer
import com.sino.jsbridge.handler.CommonJsHandler
import com.sino.jsbridge.handler.ImageJsHandler
import com.sino.jsbridge.test.R
import com.sino.jsbridge.test.test.TestJsHandler
import kotlinx.android.synthetic.main.activity_test_x5_web_view.*
import org.json.JSONObject

class TestX5WebViewActivity : AppCompatActivity(), IWebViewContainer {

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_x5_web_view)

        x5web_test.loadUrl("file:///android_asset/index.html")
//        x5web_test.loadUrl("http://172.10.189.89:8080/mall.html#/")
//        x5web_test.loadUrl("http://www.baidu.com")
//        x5web_test.loadUrl("https://debugtbs.qq.com/")

        x5web_test.getCurWebHelper().registeredJsApiHandler(this, CommonJsHandler::class.java)
        x5web_test.getCurWebHelper().registeredJsApiHandler(this, ImageJsHandler::class.java)
        x5web_test.getCurWebHelper().registeredJsApiHandler(this, TestJsHandler::class.java)
    }

    override fun onResume() {
        super.onResume()
        x5web_test.getCurWebHelper().jsEventer.event("onContainerResume")
    }

    override fun onPause() {
        super.onPause()
        val data = JSONObject()
        data.put("onContainerPause", "asdasdasd")
        x5web_test.getCurWebHelper().jsEventer.event("onContainerPause", data)
    }

    override fun closeWindow() {
        finish()
    }

    override fun updateTitle(title: String) {
        Toast.makeText(this, title, Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        x5web_test.getCurWebHelper().dispatchContainerResult(requestCode, resultCode, data)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        x5web_test.getCurWebHelper().dispatchContainerDestroy()
    }

    override fun onBackPressed() {
        if (x5web_test.visibility == View.VISIBLE && x5web_test.canGoBack()) {
            x5web_test.goBack()
        } else {
            super.onBackPressed()
        }
    }
}
