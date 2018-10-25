package com.victor.kplayer.library.module.jsevaluator

import android.webkit.JavascriptInterface
import com.victor.kplayer.library.module.jsevaluator.interfaces.CallJavaResultInterface

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by longtv, All rights reserved.
 * -----------------------------------------------------------------
 * File: JavaScriptInterface.java
 * Author: Victor
 * Date: 2018/10/23 17:05
 * Description: 
 * -----------------------------------------------------------------
 */
class JavaScriptInterface {
    private var mCallJavaResultInterface: CallJavaResultInterface

    constructor(callJavaResult: CallJavaResultInterface) {
        mCallJavaResultInterface = callJavaResult
    }

    @JavascriptInterface
    fun returnResultToJava(value: String) {
        mCallJavaResultInterface.jsCallFinished(value)
    }
}