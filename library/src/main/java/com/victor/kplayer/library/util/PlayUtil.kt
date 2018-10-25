package com.victor.kplayer.library.util

import android.text.TextUtils
import android.net.Proxy.getHost
import android.net.Uri
import java.util.regex.Pattern


/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by longtv, All rights reserved.
 * -----------------------------------------------------------------
 * File: PlayUtil.java
 * Author: Victor
 * Date: 2018/10/23 16:50
 * Description: 
 * -----------------------------------------------------------------
 */
class PlayUtil {
    companion object {
        fun getVideoId(url: String): String? {
            var videoId = url
            if (TextUtils.isEmpty(videoId)) {
                return ""
            }
            val uri = Uri.parse(videoId)
            val host = uri.getHost()

            if (!TextUtils.isEmpty(host)) {
                if (host == Constant.YOUTUBE_HOST) {
                    videoId = uri.getQueryParameter("v")
                    if (TextUtils.isEmpty(videoId)) {
                        videoId = uri.getLastPathSegment()
                    }
                } else if (host == Constant.YOUTUBE_HOST2) {
                    videoId = uri.getLastPathSegment()
                } else if (host == Constant.VIMEO_HOST) {
                    videoId = uri.getLastPathSegment()
                }
            }
            return videoId
        }

        fun getVideoType(url: String): Int {
            var videoType = 0
            if (TextUtils.isEmpty(url)) return videoType
            //播放url纯数字为vimeo video id
            if (isDigit(url)) {
                videoType = Constant.VideoType.VIMEO
            } else {
                val uri = Uri.parse(url)
                val host = uri.getHost()
                if (!TextUtils.isEmpty(host)) {
                    if (host == Constant.YOUTUBE_HOST || host == Constant.YOUTUBE_HOST2) {
                        videoType = Constant.VideoType.YOUTUBE
                    } else if (host == Constant.VIMEO_HOST) {
                        videoType = Constant.VideoType.VIMEO
                    } else if (host == Constant.FACEBOOK_HOST) {
                        videoType = Constant.VideoType.FACEBOOK
                    } else {
                        videoType = Constant.VideoType.M3U8
                    }
                } else {
                    //host 为空说明是youtube video id
                    videoType = Constant.VideoType.YOUTUBE
                }
            }
            return videoType
        }

        // 判断一个字符串是否都为数字
        private fun isDigit(strNum: String): Boolean {
            val pattern = Pattern.compile("[0-9]{1,}")
            val matcher = pattern.matcher(strNum as CharSequence)
            return matcher.matches()
        }
    }
}