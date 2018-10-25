package com.victor.kplayer.library.data

import android.util.SparseArray

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by longtv, All rights reserved.
 * -----------------------------------------------------------------
 * File: YoutubeHtmlData.java
 * Author: Victor
 * Date: 2018/10/23 16:22
 * Description: 
 * -----------------------------------------------------------------
 */
class YoutubeHtmlData {
    var encSignatures: SparseArray<String>? = null
    var decipherJsFileName: String? = null
    var ytFiles: SparseArray<YtFile>? = null
}