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
class VimeoReq {
    var title: String? = null
    var duration: Long? = null
    var streams = mutableMapOf<String,String> ()
    var thumbs = mutableMapOf<String,String> ()
    var videoUser: VimeoUser? = null
}