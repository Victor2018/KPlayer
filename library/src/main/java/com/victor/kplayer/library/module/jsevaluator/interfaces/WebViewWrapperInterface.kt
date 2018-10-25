package com.victor.kplayer.library.module.jsevaluator.interfaces

import android.webkit.WebView

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by longtv, All rights reserved.
 * -----------------------------------------------------------------
 * File: WebViewWrapperInterface.java
 * Author: Victor
 * Date: 2018/10/23 17:05
 * Description: 
 * -----------------------------------------------------------------
 */
interface WebViewWrapperInterface {
    fun loadJavaScript(javascript: String)

    // Destroys the web view in order to free the memory.
    // The web view can not be accessed after is has been destroyed.
    fun destroy()

    // Returns the WebView object
    fun getWebView(): WebView?
}