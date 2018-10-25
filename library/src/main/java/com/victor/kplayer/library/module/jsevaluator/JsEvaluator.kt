package com.victor.kplayer.library.module.jsevaluator

import android.content.Context
import android.webkit.WebView
import com.victor.kplayer.library.module.jsevaluator.interfaces.*
import java.util.concurrent.atomic.AtomicReference

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by longtv, All rights reserved.
 * -----------------------------------------------------------------
 * File: JsEvaluator.java
 * Author: Victor
 * Date: 2018/10/23 17:06
 * Description: 
 * -----------------------------------------------------------------
 */
class JsEvaluator(var context: Context): CallJavaResultInterface, JsEvaluatorInterface {
    var mWebViewWrapper: WebViewWrapperInterface? = null

    var callback = AtomicReference<JsCallback>(null)

    var mHandler: HandlerWrapperInterface = HandlerWrapper()
    companion object {
        val JS_NAMESPACE = "evgeniiJsEvaluator"
        private val JS_ERROR_PREFIX = "evgeniiJsEvaluatorException"

        fun escapeCarriageReturn(str: String): String {
            return str.replace("\r", "\\r")
        }

        fun escapeClosingScript(str: String): String {
            return str.replace("</", "<\\/")
        }

        fun escapeNewLines(str: String): String {
            return str.replace("\n", "\\n")
        }

        fun escapeSingleQuotes(str: String): String {
            return str.replace("'", "\\'")
        }

        fun escapeSlash(str: String): String {
            return str.replace("\\", "\\\\")
        }

        fun getJsForEval(jsCode: String): String {
            var jsCode = jsCode
            jsCode = escapeSlash(jsCode)
            jsCode = escapeSingleQuotes(jsCode)
            jsCode = escapeClosingScript(jsCode)
            jsCode = escapeNewLines(jsCode)
            jsCode = escapeCarriageReturn(jsCode)

            return String.format("%s.returnResultToJava(eval('try{%s}catch(e){\"%s\"+e}'));",
                    JS_NAMESPACE, jsCode, JS_ERROR_PREFIX)
        }
    }

    override fun jsCallFinished(value: String) {
        val callbackLocal = callback.getAndSet(null) ?: return

        mHandler.post(Runnable {
            if (value != null && value.startsWith(JS_ERROR_PREFIX)) {
                callbackLocal.onError(value.substring(JS_ERROR_PREFIX.length))
            } else {
                callbackLocal.onResult(value)
            }
        })
    }

    override fun callFunction(jsCode: String, resultCallback: JsCallback, name: String, vararg args: Any) {
        var code =  "$jsCode; ${JsFunctionCallFormatter.toString(name, args)}"
        evaluate(code, resultCallback)
    }

    override fun evaluate(jsCode: String, resultCallback: JsCallback) {
        val js = getJsForEval(jsCode)
        this.callback.set(resultCallback)
        getWebViewWrapper().loadJavaScript(js)
    }

    override fun destroy() {
        getWebViewWrapper().destroy()
    }

    override fun getWebView(): WebView {
        return getWebViewWrapper().getWebView()!!
    }

    fun getWebViewWrapper(): WebViewWrapperInterface {
        if (mWebViewWrapper == null) {
            mWebViewWrapper = WebViewWrapper(context, this)
        }
        return mWebViewWrapper as WebViewWrapperInterface
    }


}