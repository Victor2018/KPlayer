package com.victor.kplayer.library.interfaces

import com.victor.kplayer.library.data.FacebookReq
import com.victor.kplayer.library.data.VimeoReq
import com.victor.kplayer.library.data.YoutubeReq

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by longtv, All rights reserved.
 * -----------------------------------------------------------------
 * File: OnExtractListener.java
 * Author: Victor
 * Date: 2018/10/25 10:08
 * Description: 
 * -----------------------------------------------------------------
 */
interface OnExtractListener {
    fun OnYoutube (youtubeReq: YoutubeReq?)
    fun OnVimeo (vimeoReq: VimeoReq?)
    fun OnFacebook (facebookReq: FacebookReq?)
}