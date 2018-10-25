package com.victor.kplayer.library.interfaces

import com.victor.kplayer.library.data.YoutubeReq

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by longtv, All rights reserved.
 * -----------------------------------------------------------------
 * File: OnYoutubeListener.java
 * Author: Victor
 * Date: 2018/10/24 10:17
 * Description: 
 * -----------------------------------------------------------------
 */
interface OnYoutubeListener {
    fun OnYoutube (youtubeReq: YoutubeReq?,msg: String)
}