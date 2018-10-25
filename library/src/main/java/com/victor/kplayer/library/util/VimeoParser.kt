package com.victor.kplayer.library.util

import com.victor.kplayer.library.data.VimeoReq
import com.victor.kplayer.library.data.VimeoUser
import org.json.JSONException
import org.json.JSONObject

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by longtv, All rights reserved.
 * -----------------------------------------------------------------
 * File: VimeoParser.java
 * Author: Victor
 * Date: 2018/10/24 14:56
 * Description: 
 * -----------------------------------------------------------------
 */
class VimeoParser {
    companion object {
        fun parseVimeo(response: String?): VimeoReq {
            var vimeoReq = VimeoReq()
            try {
                //Turn JSON string to object
                val requestJson = JSONObject(response)

                //Access video information
                val videoInfo = requestJson.getJSONObject("video")
                vimeoReq.duration = videoInfo.getLong("duration")
                vimeoReq.title = videoInfo.getString("title")

                //Get user information
                val userInfo = videoInfo.getJSONObject("owner")
                var vimeoUser = VimeoUser ()
                vimeoUser.accountType = userInfo.optString("account_type")
                vimeoUser.name = userInfo.optString("name")
                vimeoUser.imageUrl = userInfo.optString("img")
                vimeoUser.image2xUrl = userInfo.optString("img_2x")
                vimeoUser.url = userInfo.optString("url")
                vimeoUser.id = userInfo.optLong("id")
                vimeoReq.videoUser = vimeoUser

                //Get thumbnail information
                val thumbsInfo = videoInfo.getJSONObject("thumbs")
                val iterator: Iterator<String>
                iterator = thumbsInfo.keys()
                while (iterator.hasNext()) {
                    val key = iterator.next()
                    vimeoReq.thumbs.put(key, thumbsInfo.getString(key))
                }

                //Access video stream information
                val streamArray = requestJson.getJSONObject("request")
                        .getJSONObject("files")
                        .getJSONArray("progressive")

                //Get info for each stream available
                for (streamIndex in 0 until streamArray.length()) {
                    val stream = streamArray.getJSONObject(streamIndex)
                    val url = stream.getString("url")
                    val quality = stream.getString("quality")
                    //Store stream information
                    vimeoReq.streams.put(quality, url)
                }

            } catch (e: JSONException) {
                e.printStackTrace()
            }
            return vimeoReq
        }
    }
}