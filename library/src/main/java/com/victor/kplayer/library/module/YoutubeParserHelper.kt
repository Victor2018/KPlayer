package com.victor.kplayer.library.module

import android.text.TextUtils
import com.victor.kplayer.library.util.YoutubeParser
import com.victor.kplayer.library.data.FmtStreamMap
import android.R.attr.thumb
import android.R.attr.rating
import android.R.attr.author
import android.content.Context
import android.os.Handler
import com.victor.kplayer.library.data.YoutubeReq
import android.os.HandlerThread
import android.os.Message
import android.util.Log
import android.util.SparseArray
import com.victor.kplayer.library.data.DecipherViaParm
import com.victor.kplayer.library.data.YtFile
import com.victor.kplayer.library.interfaces.OnHttpListener
import com.victor.kplayer.library.interfaces.OnYoutubeListener
import com.victor.kplayer.library.util.PlayUtil
import com.victor.kplayer.library.util.YoutubeAction
import java.lang.ref.WeakReference


/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by longtv, All rights reserved.
 * -----------------------------------------------------------------
 * File: YoutubeParserHelper.java
 * Author: Victor
 * Date: 2018/10/23 15:47
 * Description: 
 * -----------------------------------------------------------------
 */
class YoutubeParserHelper: OnHttpListener {

    companion object {
        const val REQUEST_YOUTUBE_INFO = 0x8001
        const val REQUEST_YOUTUBE_HTML = 0x8002
        const val REQUEST_YOUTUBE_DECIPHER = 0x8003
        const val REQUEST_YOUTUBE_DECIPHER_VIA = 0x8004
    }

    private var refContext: WeakReference<Context>? = null
    private var ytFiles: SparseArray<YtFile>? = null
    private var youtubeUrl: String? = null
    private var identifier: String? = null
    private var youtubeReq: YoutubeReq? = null
    private var mOnYoutubeListener: OnYoutubeListener? = null

    var decipherViaParm = DecipherViaParm()

    private val TAG = "HttpRequestHelper"
    private var mRequestHandler: Handler? = null
    private var mRequestHandlerThread: HandlerThread? = null

    //由于primary constructor不能包含任何代码，因此使用 init 代码块对其初始化，同时可以在初始化代码块中使用构造函数的参数
    init {
        startRequestTask()
    }
    constructor(context: Context,listener: OnYoutubeListener) {
        refContext = WeakReference(context)
        mOnYoutubeListener = listener
    }

    private fun startRequestTask() {
        mRequestHandlerThread = HandlerThread("YoutubeParserHelper")
        mRequestHandlerThread!!.start()
        mRequestHandler = object : Handler(mRequestHandlerThread!!.looper) {
            override fun handleMessage(msg: Message) {
                val parmMap: HashMap<Int, Any>?
                when (msg.what) {
                    REQUEST_YOUTUBE_INFO -> {
                        retData()
                        parmMap = msg.obj as HashMap<Int, Any>?
                        if (parmMap == null) return
                        youtubeUrl = parmMap!![REQUEST_YOUTUBE_INFO] as String
                        identifier = PlayUtil.getVideoId(youtubeUrl!!)
                        YoutubeAction.requestYoutubeInfo(identifier!!, this@YoutubeParserHelper)
                    }
                    REQUEST_YOUTUBE_HTML -> {
                        parmMap = msg.obj as HashMap<Int, Any>?
                        if (parmMap == null) return
                        youtubeUrl = parmMap!![REQUEST_YOUTUBE_HTML] as String
                        identifier = PlayUtil.getVideoId(youtubeUrl!!)
                        YoutubeAction.requestYoutubeHtml(identifier!!, this@YoutubeParserHelper)
                    }
                    REQUEST_YOUTUBE_DECIPHER -> {
                        parmMap = msg.obj as HashMap<Int, Any>?
                        if (parmMap == null) return
                        val decipherJsFileName = parmMap!![REQUEST_YOUTUBE_DECIPHER] as String
                        YoutubeAction.requestYoutubeDecipher(decipherJsFileName, this@YoutubeParserHelper)
                    }
                    REQUEST_YOUTUBE_DECIPHER_VIA -> {
                        parmMap = msg.obj as HashMap<Int, Any>?
                        if (parmMap == null) return
                        val parm = parmMap!![REQUEST_YOUTUBE_DECIPHER_VIA] as DecipherViaParm
                        YoutubeAction.decipherViaWebView(refContext!!.get()!!, parm, this@YoutubeParserHelper)
                    }
                }
            }
        }
    }

    fun retData () {
        ytFiles = null
        youtubeReq = null
        youtubeUrl = null
        identifier = null
    }

    @Synchronized
    fun sendRequestWithParms(Msg: Int, requestData: Any?) {
        val requestMap = HashMap<Int, Any>()
        requestMap.put(Msg, requestData!!)
        val msg = mRequestHandler!!.obtainMessage(Msg, requestMap)
        mRequestHandler!!.sendMessage(msg)
    }

    @Synchronized
    fun sendRequest(msg: Int) {
        mRequestHandler!!.sendEmptyMessage(msg)
    }

    @Synchronized
    fun onDestroy() {
        if (mRequestHandlerThread != null) {
            mRequestHandlerThread!!.quit()
            mRequestHandlerThread = null
        }
    }

    override fun onComplete(videoType: Int, data: Any?, msg: String) {
        when (videoType) {
            REQUEST_YOUTUBE_INFO -> {
                youtubeReq = YoutubeParser.parseYoutubeData(data.toString())
                for (item in youtubeReq?.sm!!) {
                    Log.e(TAG, item.itag + "-" + item.quality + "-->" + item.url)
                }

                val sigEnc = YoutubeParser.parseYoutubeSigEnc(data.toString())
                Log.e(TAG, "onComplete-sigEnc = " + sigEnc)
                if (sigEnc) {
                    sendRequestWithParms(REQUEST_YOUTUBE_HTML, youtubeUrl)
                } else {
                    mOnYoutubeListener?.OnYoutube(youtubeReq,"parse youtube data error!")
                }
            }
            REQUEST_YOUTUBE_HTML -> {
                val youtubeHtmlData = YoutubeParser.parseYoutubeHtml(data.toString())
                ytFiles = youtubeHtmlData!!.ytFiles
                decipherViaParm.encSignatures = youtubeHtmlData!!.encSignatures
                sendRequestWithParms(REQUEST_YOUTUBE_DECIPHER, youtubeHtmlData!!.decipherJsFileName)
            }
            REQUEST_YOUTUBE_DECIPHER -> {
                val decipherData = YoutubeParser.parseYoutubeDecipher(data.toString())
                decipherViaParm.decipherFunctionName = decipherData!!.decipherFunctionName
                decipherViaParm.decipherFunctions = decipherData!!.decipherFunctions
                Log.e(TAG, "onComplete-decipherData.decipherFunctionName = " + decipherData!!.decipherFunctionName)
                Log.e(TAG, "onComplete-decipherData.decipherFunctions = " + decipherData!!.decipherFunctions)
                sendRequestWithParms(REQUEST_YOUTUBE_DECIPHER_VIA, decipherViaParm)
            }
            REQUEST_YOUTUBE_DECIPHER_VIA -> {
                val signature = data.toString()
                Log.e(TAG, "onComplete-jsResult = $signature")
                if (!TextUtils.isEmpty(signature)) {
                    val sigs = signature.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    var i = 0
                    var urls = mutableListOf<FmtStreamMap>()
                    while (i < decipherViaParm.encSignatures!!.size() && i < sigs.size) {
                        val key = decipherViaParm.encSignatures!!.keyAt(i)
                        if (key == 0) {
                            //                            dashMpdUrl = dashMpdUrl.replace("/s/" + encSignatures.get(key), "/signature/" + sigs[i]);
                            //                            Log.e(TAG,"getStreamUrls()......dashMpdUrl = " + dashMpdUrl);
                        } else {
                            if (ytFiles != null && ytFiles!!.size() > 0) {
                                var url = ytFiles!!.get(key).getUrl()
                                url += "&signature=" + sigs[i]
                                Log.e(TAG, "getStreamUrls()......url = $key------>$url")
                                if (i < youtubeReq?.sm!!.size) {
                                    if (key == 22 || key == 18) {
                                        val streamMap = FmtStreamMap()
                                        streamMap.itag = key.toString();
                                        streamMap.url = url
                                        urls.add(streamMap)
                                    }
                                }
                            }
                        }
                        i++
                    }
                    youtubeReq?.sm = urls
                    mOnYoutubeListener?.OnYoutube(youtubeReq,"youtube no data response!")
                }
            }
        }
    }

}