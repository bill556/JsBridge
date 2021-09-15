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
            "getToken" -> {
                val data = JSONObject()
                data.put("token", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0ZW5hbnRfaWQiOiIwMDAwMDAiLCJhY2NvdW50X3R5cGUiOjIxLCJ1c2VyX25hbWUiOiIxNzc2MzYyNDcxMCIsInJlYWxfbmFtZSI6IjE3NzYzNjI0NzEwIiwiYXZhdGFyIjoiaHR0cHM6Ly90aGlyZHd4LnFsb2dvLmNuL21tb3Blbi92aV8zMi9RMGo0VHdHVGZUSkxBeGlhN0ppY3RYbVducm11dnZEQ1hRZXYxRnJhQWRNY3RkYXEwQUdOZEhVd3NZWjFLaWFqSHlxbndUNVdvWHlGSzRZNGgweUN4SGZwQS8xMzIiLCJvYmplY3RfaWQiOiIxMzcxMzYwMzkxMzcyMDk5NTg1IiwiYXV0aG9yaXRpZXMiOlsid2VpeGluX3VzZXIiXSwiY2xpZW50X2lkIjoic2luby1wbGF0ZnJvbSIsInJvbGVfbmFtZSI6IndlaXhpbl91c2VyIiwibGljZW5zZSI6InBvd2VyZWQgYnkgc2lub2NhcmUuY29tIiwiYXVkIjpbInt9Il0sIm9yZ19ndWlkIjoiIiwidXNlcl9pZCI6IjEzNzEzNjAzOTExMjI0NTY1NzgiLCJyb2xlX2lkIjoiMTE3ODIxMzEyMTg1MTE5OTQ5MCIsInBob25lIjoiMTc3NjM2MjQ3MTAiLCJvcmdfaWQiOiIiLCJzY29wZSI6WyJhbGwiXSwibmlja19uYW1lIjoiQmlsbDEiLCJndWlkIjoiIiwiZXhwIjoxNjMwMDI5MDAxLCJlbXBsb3llZV93b3JrbnVtIjoiIiwianRpIjoiNjRlYzI4MjgtYTcwZS00YTExLWIwNzYtODU5NjFkMmU3OGFjIiwiYWNjb3VudCI6IjE3NzYzNjI0NzEwIn0.zBKbSVmDLhieMTWxoN4qReQAJHnS6P1U5Rcey7tiO3g")
                jsCallBacker.success(data)
                return true
            }
        }
        return false
    }
}