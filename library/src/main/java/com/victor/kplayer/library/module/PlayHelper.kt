package com.victor.kplayer.library.module

import android.content.Context
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.SurfaceView
import android.view.TextureView
import com.victor.kplayer.library.data.FacebookReq
import com.victor.kplayer.library.data.SubTitleInfo
import com.victor.kplayer.library.data.VimeoReq
import com.victor.kplayer.library.data.YoutubeReq
import com.victor.kplayer.library.interfaces.OnExtractListener
import com.victor.kplayer.library.interfaces.OnYoutubeListener
import com.victor.kplayer.library.interfaces.OnYoutubeSubTitleListener
import com.victor.kplayer.library.presenter.FacebookPresenterImpl
import com.victor.kplayer.library.presenter.VimeoPresenterImpl
import com.victor.kplayer.library.util.Constant
import com.victor.kplayer.library.util.PlayUtil
import com.victor.kplayer.library.view.FacebookView
import com.victor.kplayer.library.view.VimeoView
import java.util.HashMap

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by longtv, All rights reserved.
 * -----------------------------------------------------------------
 * File: PlayHelper.java
 * Author: Victor
 * Date: 2018/10/24 17:55
 * Description: 
 * -----------------------------------------------------------------
 */
class PlayHelper: OnYoutubeListener,OnYoutubeSubTitleListener, VimeoView, FacebookView {
    override fun OnYoutubeSubTitle(datas: HashMap<Int, SubTitleInfo>?, msg: String) {
        mPlayer?.setSubTitle(datas!!)
    }

    private var youtubeReq: YoutubeReq? = null
    private var vimeoReq: VimeoReq? = null
    private var facebookReq: FacebookReq? = null
    private var playUrl: String? = null
    private var mOnExtractListener: OnExtractListener? = null

    override fun OnFacebook(data: FacebookReq?, msg: String) {
        facebookReq = data
        mOnExtractListener?.OnFacebook(facebookReq)
        mPlayer?.playUrl(data?.playUrl,false)
    }

    override fun OnVimeo(data: VimeoReq?, msg: String) {
        vimeoReq = data
        mOnExtractListener?.OnVimeo(vimeoReq)
        var quality = "1080p"
        var playUrl = data?.streams?.get("1080p")
        if (TextUtils.isEmpty(playUrl)) {
            playUrl = data?.streams?.get("720p")
            quality = "720p"
        }
        if (TextUtils.isEmpty(playUrl)) {
            playUrl = data?.streams?.get("540p")
            quality = "540p"
        }
        if (TextUtils.isEmpty(playUrl)) {
            playUrl = data?.streams?.get("360p")
            quality = "360p"
        }
        if (TextUtils.isEmpty(playUrl)) {
            playUrl = data?.streams?.get("270p")
            quality = "270p"
        }
        Log.e(TAG, "OnVimeo-quality------>$quality")
        Log.e(TAG, "OnVimeo-playUrl------>$playUrl")
        mPlayer?.playUrl(playUrl,false)
    }

    override fun OnYoutube(youtubeReq: YoutubeReq?, msg: String) {
        this.youtubeReq = youtubeReq
        mOnExtractListener?.OnYoutube(youtubeReq)
        if (TextUtils.isEmpty(youtubeReq?.hlsvp)) {
            Log.e(TAG,"OnYoutube-url =" + youtubeReq?.sm?.get(0)?.itag + "--->" + youtubeReq?.sm?.get(0)?.url)
            mPlayer?.playUrl(youtubeReq?.sm?.get(0)?.url,false)
        } else {
            Log.e(TAG,"OnYoutube-hlsvp = " + youtubeReq?.hlsvp)
            mPlayer?.playUrl(youtubeReq?.hlsvp,true)
        }
    }

    constructor(context: Context,surfaceView: SurfaceView,handler: Handler?) {
        mContext = context
        mSurfaceView = surfaceView
        mHandler = handler
        initialize()
    }
    constructor(context: Context,textureView: TextureView,handler: Handler?) {
        mContext = context
        mTextureView = textureView
        mHandler = handler
        initialize()
    }
    fun initialize () {
        youtubeParserHelper = YoutubeParserHelper(mContext!!,this,this)
        vimeoPresenter = VimeoPresenterImpl(this)
        facebookPresenter = FacebookPresenterImpl(this)
        if (mTextureView != null) {
            mPlayer = Player(mTextureView!!, mHandler)
        } else {
            mPlayer = Player(mSurfaceView!!, mHandler)
        }
    }
    companion object {
        var TAG = "PlayHelper"
        var mContext:Context? = null
        var mHandler: Handler? = null
        var youtubeParserHelper: YoutubeParserHelper? = null
        var vimeoPresenter: VimeoPresenterImpl? = null
        var facebookPresenter: FacebookPresenterImpl? = null
        var mPlayer: Player? = null
        var mSurfaceView: SurfaceView? = null
        var mTextureView: TextureView? = null

    }

    fun play (url: String,listener: OnExtractListener?) {
        mOnExtractListener = listener
        playUrl = url
        retData()
        var videoType = PlayUtil.getVideoType(url)
        var videoId = PlayUtil.getVideoId(url)
        when(videoType) {
            Constant.VideoType.YOUTUBE -> {
                youtubeParserHelper?.sendRequestWithParms(YoutubeParserHelper.REQUEST_YOUTUBE_INFO, videoId)
            }
            Constant.VideoType.VIMEO -> {
                vimeoPresenter?.sendRequest(String.format(Constant.VIMEO_CONFIG_URL, videoId), Constant.getVimeoHttpHeaderParm(videoId), null)
            }
            Constant.VideoType.FACEBOOK -> {
                facebookPresenter?.sendRequest(videoId!!,null, null)
            }
            Constant.VideoType.M3U8 -> {
                mPlayer?.playUrl(url, false)
            }
        }
    }

    fun getData (): Any? {
        when (PlayUtil.getVideoType(playUrl!!)) {
            Constant.VideoType.YOUTUBE -> {
                return getYoutubeReq()
            }
            Constant.VideoType.VIMEO -> {
                return getVimeoReq()
            }
            Constant.VideoType.FACEBOOK -> {
                return getFacebookReq()
            }
        }
        return null
    }

    fun retData () {
        youtubeReq = null
        vimeoReq = null
        facebookReq = null
    }

    fun getYoutubeReq (): YoutubeReq? {
        return youtubeReq
    }

    fun getVimeoReq (): VimeoReq? {
        return vimeoReq
    }

    fun getFacebookReq (): FacebookReq? {
        return facebookReq
    }

    fun onResume() {
        mPlayer?.resume()
    }

    fun onPause() {
        mPlayer?.pause()
    }

    fun onDestroy() {
        retData()
        mPlayer?.stop()
        mPlayer?.close()
        mPlayer = null

        youtubeParserHelper?.onDestroy()
        youtubeParserHelper = null

        vimeoPresenter?.detachView()
        vimeoPresenter = null

        facebookPresenter?.detachView()
        facebookPresenter = null
    }

}