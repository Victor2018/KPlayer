package com.victor.kplayer.library.module.jsevaluator

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.Base64
import android.util.Log
import android.webkit.WebView
import com.victor.kplayer.library.module.jsevaluator.interfaces.CallJavaResultInterface
import com.victor.kplayer.library.module.jsevaluator.interfaces.WebViewWrapperInterface
import java.io.UnsupportedEncodingException

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by longtv, All rights reserved.
 * -----------------------------------------------------------------
 * File: WebViewWrapper.java
 * Author: Victor
 * Date: 2018/10/23 17:09
 * Description: 
 * -----------------------------------------------------------------
 */
@SuppressLint("SetJavaScriptEnabled")
class WebViewWrapper: WebViewWrapperInterface {
    protected var mWebView: WebView? = null
    var TAG = "WebViewWrapper"
    constructor(context: Context, callJavaResult: CallJavaResultInterface) {
        mWebView = WebView(context)

        // web view will not draw anything - turn on optimizations
        mWebView!!.setWillNotDraw(true)

        val webSettings = mWebView!!.settings
        webSettings.javaScriptEnabled = true
        webSettings.defaultTextEncodingName = "utf-8"
        val jsInterface = JavaScriptInterface(callJavaResult)
        mWebView!!.addJavascriptInterface(jsInterface, JsEvaluator.JS_NAMESPACE)
    }

    override fun loadJavaScript(javascript: String) {
        Log.e(TAG,"loadJavaScript()......")
        var javascript = javascript
        val data: ByteArray
        try {
            javascript = "<script>$javascript</script>"
            data = javascript.toByteArray(charset("UTF-8"))
            val base64 = Base64.encodeToString(data, Base64.DEFAULT)
            mWebView!!.loadUrl("data:text/html;charset=utf-8;base64,$base64")
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }

    }

    // Destroys the web view in order to free the memory
    // The web view can not be accessed after is has been destroyed
    // To check open the page in Chrome: chrome://inspect/#devices
    override fun destroy() {
        if (mWebView != null) {
            mWebView!!.removeJavascriptInterface(JsEvaluator.JS_NAMESPACE)
            mWebView!!.loadUrl("about:blank")
            mWebView!!.stopLoading()

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                mWebView!!.freeMemory()
            }

            mWebView!!.clearHistory()
            mWebView!!.removeAllViews()
            mWebView!!.destroyDrawingCache()
            mWebView!!.destroy()

            mWebView = null
        }
    }

    // Returns the WebView object
    override fun getWebView(): WebView? {
        return mWebView
    }
}