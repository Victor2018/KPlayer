package com.victor.kplayer.library.module.jsevaluator.interfaces

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by longtv, All rights reserved.
 * -----------------------------------------------------------------
 * File: JsCallback.java
 * Author: Victor
 * Date: 2018/10/23 17:03
 * Description: 
 * -----------------------------------------------------------------
 */
interface JsCallback {
    fun onResult(value: String)
    fun onError(errorMessage: String)
}