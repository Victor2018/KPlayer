package com.victor.kplayer.library.module.jsevaluator

import android.os.Handler
import com.victor.kplayer.library.module.jsevaluator.interfaces.HandlerWrapperInterface

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by longtv, All rights reserved.
 * -----------------------------------------------------------------
 * File: HandlerWrapper.java
 * Author: Victor
 * Date: 2018/10/23 17:00
 * Description: 
 * -----------------------------------------------------------------
 */
class HandlerWrapper: HandlerWrapperInterface {
    private var mHandler: Handler = Handler()

    override fun post(r: Runnable) {
        mHandler.post(r)
    }
}