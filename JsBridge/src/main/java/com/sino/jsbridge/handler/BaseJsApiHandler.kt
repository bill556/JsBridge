package com.sino.jsbridge.handler

import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.sino.jsbridge.bridge.JsCallBacker
import com.sino.jsbridge.cotainer.IWebViewContainer
import java.lang.ref.WeakReference

abstract class BaseJsApiHandler : IJsApiHandler {

    private var activityWR: WeakReference<FragmentActivity>? = null
    private var fragmentWR: WeakReference<Fragment?>? = null
    private var containerWR: WeakReference<IWebViewContainer>? = null

    override fun onAttachContainer(activity: FragmentActivity) {
        activityWR = WeakReference(activity)
        check(activity is IWebViewContainer) { "activity Must implement IZWebViewContainer" }
        containerWR = WeakReference(activity)
    }

    override fun onAttachContainer(fragment: Fragment) {
        activityWR = WeakReference(fragment.requireActivity())
        fragmentWR = WeakReference(fragment)
        check(fragment is IWebViewContainer) { "fragment Must implement IZWebViewContainer" }
        containerWR = WeakReference(fragment)
    }

    fun getActivity(): FragmentActivity? {
        return activityWR?.get()
    }

    fun getFragment(): Fragment? {
        return fragmentWR?.get()
    }

    fun <T : IWebViewContainer> getContainerOp(): T? {
        @Suppress("UNCHECKED_CAST")
        return containerWR?.get() as T?
    }

    private val jsCallBackerMap: MutableMap<String, JsCallBacker> by lazy {
        mutableMapOf<String, JsCallBacker>()
    }

    fun saveJsCallBacker(key: String, JsCallBacker: JsCallBacker) {
        jsCallBackerMap[key] = JsCallBacker
    }

    fun get7RemoveJsCallBacker(key: String): JsCallBacker? {
        return jsCallBackerMap.remove(key)
    }

    @CallSuper
    override fun onContainerDestroy() {
        jsCallBackerMap.clear()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}