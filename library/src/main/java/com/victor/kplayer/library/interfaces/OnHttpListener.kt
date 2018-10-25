package com.victor.kplayer.library.interfaces

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by longtv, All rights reserved.
 * -----------------------------------------------------------------
 * File: OnHttpListener.java
 * Author: Victor
 * Date: 2018/10/23 16:31
 * Description: 
 * -----------------------------------------------------------------
 */
interface OnHttpListener {
    abstract fun onComplete(videoType: Int, data: Any?, msg: String)
}