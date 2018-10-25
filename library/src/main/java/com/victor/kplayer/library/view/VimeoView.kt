package com.victor.kplayer.library.view

import com.victor.kplayer.library.data.VimeoReq

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by longtv, All rights reserved.
 * -----------------------------------------------------------------
 * File: VimeoView.java
 * Author: Victor
 * Date: 2018/10/23 11:02
 * Description: 
 * -----------------------------------------------------------------
 */
interface VimeoView {
    fun OnVimeo (data: VimeoReq?,msg: String)
}