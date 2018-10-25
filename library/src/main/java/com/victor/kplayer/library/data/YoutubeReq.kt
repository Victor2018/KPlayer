package com.victor.kplayer.library.data

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by longtv, All rights reserved.
 * -----------------------------------------------------------------
 * File: YoutubeReq.java
 * Author: Victor
 * Date: 2018/10/23 14:21
 * Description: 
 * -----------------------------------------------------------------
 */
class YoutubeReq {
    var have_basic: Boolean = false
    var have_gdata: Boolean = false
    var description: String? = null
    var category: String? = null
    var channelId: String? = null
    var hlsvp: String? = null//直播m3u8播放地址（为空则是点播）
    var published: String? = null
    var sm: List<FmtStreamMap>? = null
    var asm: List<FmtStreamMap>? = null
    var jsurl: String? = null
    // streams
    // oggstreams
    // m4astreams
    // allstreams
    // videostreams
    // audiostreams
    var title: String? = null
    var thumb: String? = null
    var rating: String? = null
    var length: Long = 0
    var author: String? = null
    var formats: String? = null
    var videoid: String? = null
    var ciphertag: Boolean = false
    var duration: String? = null
    var keywords: Array<String>? = null
    var bigthumb: String? = null
    var viewcount: Int = 0
    var bigthumbhd: String? = null
}