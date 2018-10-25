package com.victor.kplayer.library.util

import android.content.Context
import android.util.Log
import com.victor.kplayer.library.data.DecipherViaParm
import com.victor.kplayer.library.interfaces.OnHttpListener
import com.victor.kplayer.library.module.YoutubeParserHelper
import com.victor.kplayer.library.module.jsevaluator.JsEvaluator
import com.victor.kplayer.library.module.jsevaluator.interfaces.JsCallback
import org.victor.khttp.library.util.HttpUtil
import java.net.SocketTimeoutException

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by longtv, All rights reserved.
 * -----------------------------------------------------------------
 * File: YoutubeAction.java
 * Author: Victor
 * Date: 2018/10/23 16:30
 * Description: 
 * -----------------------------------------------------------------
 */
class YoutubeAction {
    companion object {
        const val TAG = "YoutubeAction"
        @Synchronized
        fun requestYoutubeInfo(identifier: String, listener: OnHttpListener?) {
            Log.e(TAG, "requestYoutubeInfo()......identifier = $identifier")
            val msg = ""
            var data = ""
            try {
                val requestUrl = String.format(Constant.YOUTUBE_URL, identifier)
                data = HttpUtil.get(requestUrl)
            } catch (e: SocketTimeoutException) {
                e.printStackTrace()
            }

            if (listener != null) {
                listener!!.onComplete(YoutubeParserHelper.REQUEST_YOUTUBE_INFO, data, msg)
            }
        }

        @Synchronized
        fun requestYoutubeHtml(identifier: String, listener: OnHttpListener?) {
            Log.e(TAG, "requestYoutubeHtml()......identifier = $identifier")
            val msg = ""
            var data = ""
            try {
                val requestUrl = String.format(Constant.WATCHV_HTTPS, identifier)
                Log.e(TAG, "requestYoutubeHtml()......requestUrl = $requestUrl")
                data = HttpUtil.get(requestUrl)
                data = data.replace("\\u0026", "&")
            } catch (e: SocketTimeoutException) {
                e.printStackTrace()
            }

            if (listener != null) {
                listener!!.onComplete(YoutubeParserHelper.REQUEST_YOUTUBE_HTML, data, msg)
            }
        }

        @Synchronized
        fun requestYoutubeDecipher(decipherJsFileName: String, listener: OnHttpListener?) {
            Log.e(TAG, "requestYoutubeDecipher()......decipherJsFileName = $decipherJsFileName")
            val msg = ""
            var data = ""
            try {
                val requestUrl = String.format(Constant.DECIPHER_URL, decipherJsFileName)
                Log.e(TAG, "requestYoutubeDecipher()......requestUrl = $requestUrl")
                data = HttpUtil.get(requestUrl)
            } catch (e: SocketTimeoutException) {
                e.printStackTrace()
            }

            if (listener != null) {
                listener!!.onComplete(YoutubeParserHelper.REQUEST_YOUTUBE_DECIPHER, data, msg)
            }
        }

        @Synchronized
        fun decipherViaWebView(context: Context, decipherViaParm: DecipherViaParm?, listener: OnHttpListener?) {
            Log.e(TAG, "decipherViaWebView()......")
            if (decipherViaParm == null) return

            val stb = StringBuilder(decipherViaParm!!.decipherFunctions + " function decipher(")
            stb.append("){return ")

            for (i in 0 until decipherViaParm!!.encSignatures!!.size()) {
                val key = decipherViaParm!!.encSignatures!!.keyAt(i)
                if (i < decipherViaParm!!.encSignatures!!.size() - 1)
                    stb.append(decipherViaParm!!.decipherFunctionName).append("('").append(decipherViaParm!!.encSignatures!!.get(key)).append("')+\"\\n\"+")
                else
                    stb.append(decipherViaParm!!.decipherFunctionName).append("('").append(decipherViaParm!!.encSignatures!!.get(key)).append("')")
            }
            stb.append("};decipher();")
            Log.e(TAG, "decipherViaWebView()......jsCode = " + stb.toString())
            JsEvaluator(context).evaluate(stb.toString(), object : JsCallback {
                override fun onResult(result: String) {
                    Log.e(TAG, "decipherViaWebView()......JsEvaluator-onResult = $result")
                    if (listener != null) {
                        listener!!.onComplete(YoutubeParserHelper.REQUEST_YOUTUBE_DECIPHER_VIA, result, "")
                    }
                }
                override fun onError(errorMessage: String) {
                    Log.e(TAG, errorMessage)
                    if (listener != null) {
                        listener!!.onComplete(YoutubeParserHelper.REQUEST_YOUTUBE_DECIPHER_VIA, null, errorMessage)
                    }
                }
            })
        }
    }
}