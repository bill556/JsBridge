JsBridge
===
> 参考微信jsBridge的一套完整的native-bridge-web协议与实现，清晰规范的开发Hybrid App

Support **API v19+**  
Support **androidx**

#### JsBridge能做什么
- 对web端提供js-sdk，形成sdk概念，统一app对外api，统一的api管理（权限、版本兼容）
- 让native端api具备组件化能力，不在需要将所有api写个一个modlue中
- 保障native-web js交互的数据一致性、安全

#### 什么场景下需要使用JsBridge
- 项目中有较多的web与native交互，需要native统一提供对外api
- native组件化，需要在不同模块中实现api逻辑

#### 添加依赖  [![](https://jitpack.io/v/bill556/JsBridge.svg)](https://jitpack.io/#bill556/JsBridge)
```groovy
maven { url "https://jitpack.io" }

implementation 'com.github.bill556:JsBridge:1.0.0'
```

#### 需要修改的类
- Webview（android、x5...）
- WebViewClient（添加一些方法调用，协助js感知webview生命周期）
- activity|fragment
    - 注册api实现类
    - 添加一些方法，协助js感知容器生命周期

#### WebView implements IWebView
```kotlin
class WebView : WebView, IZWebView {
    
    private val WebHelper: WebHelper by lazy { WebHelper(this) }

    override fun getCurUrl(): String {
        return url
    }

    override fun getCurContext(): Context {
        return context
    }

    override fun getCurWebHelper(): WebHelper {
        return zWebHelper
    }

    override fun execJs(methodName: String, params: String?, valueCallback: ValueCallback<String>?) {
        val js: String = if (params.isNullOrBlank()) {
            String.format("%s()", methodName)
        } else {
            String.format("%s('%s')", methodName, params)
        }
        execJs(js, valueCallback)
    }

    override fun execJs(sourceJs: String, valueCallback: ValueCallback<String>?) {
        if (JsBridge.JS_DEBUG) JsBridge.log("evaluateJavascript:javascript:$sourceJs")
        runOnMainThread(Runnable {
            evaluateJavascript("javascript:$sourceJs") { valueCallback?.onReceiveValue(it) }
        })
    }

    override fun runOnMainThread(runnable: Runnable) {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            runnable.run()
            return
        }
        post(runnable)
    }
}
```

#### Webview addJavascriptInterface
```kotlin
addJavascriptInterface(JavascriptInterface(this), JavascriptInterface.INTERFACE_NAME)
```

#### WebViewClient
```kotlin
private inner class InnerCustomWebViewClient : WebViewClient() {
    override fun onPageFinished(webView: WebView?, s: String?) {
        super.onPageFinished(webView, s)
        WebHelper.injectCoreJs()
    }

    override fun doUpdateVisitedHistory(p0: WebView?, p1: String?, p2: Boolean) {
        super.doUpdateVisitedHistory(p0, p1, p2)
        WebHelper.injectCoreJs()
    }
}
```

#### registeredJsApiHandler
```kotlin
web_test.getCurWebHelper().registeredJsApiHandler(this, CommonJsHandler::class.java)
web_test.getCurWebHelper().registeredJsApiHandler(this, ImageJsHandler::class.java)
```

#### activity|fragment容器 implements IWebViewContainer
```kotlin
override fun closeWindow() {
    finish()
}

override fun updateTitle(title: String) {
    tv_test_tile.text = title
}

override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    web_test.getCurWebHelper().dispatchContainerResult(requestCode, resultCode, data)
}

override fun onDetachedFromWindow() {
    super.onDetachedFromWindow()
    web_test.getCurWebHelper().dispatchContainerDestroy()
}

override fun onBackPressed() {
    if (web_test.canGoBack()) {
        web_test.goBack()
    } else {
        super.onBackPressed()
    }
}
```

