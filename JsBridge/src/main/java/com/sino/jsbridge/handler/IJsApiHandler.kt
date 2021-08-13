package com.sino.jsbridge.handler

import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.sino.jsbridge.bridge.JsCallBacker


interface IJsApiHandler {

    fun handleApi(apiName: String, params: String, jsCallBacker: JsCallBacker): Boolean

    fun onAttachContainer(activity: FragmentActivity)

    fun onAttachContainer(fragment: Fragment)

    fun onContainerResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        return false
    }

    fun onContainerDestroy()
}
