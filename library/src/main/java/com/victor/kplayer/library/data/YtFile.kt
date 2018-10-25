package com.victor.kplayer.library.data

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by longtv, All rights reserved.
 * -----------------------------------------------------------------
 * File: YtFile.java
 * Author: Victor
 * Date: 2018/10/23 15:49
 * Description: 
 * -----------------------------------------------------------------
 */
class YtFile {
    private var format: Format? = null
    private var url = ""

    constructor(format: Format, url: String) {
        this.format = format
        this.url = url
    }

    /**
     * The url to download the file.
     */
    fun getUrl(): String? {
        return url
    }

    /**
     * Format data for the specific file.
     */
    fun getFormat(): Format? {
        return format
    }

    /**
     * Format data for the specific file.
     */
    @Deprecated("")
    fun getMeta(): Format? {
        return format
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false

        val ytFile = o as YtFile?

        if (if (format != null) !format!!.equals(ytFile!!.format) else ytFile!!.format != null) return false
        return if (url != null) url == ytFile.url else ytFile.url == null
    }

    override fun hashCode(): Int {
        var result = if (format != null) format!!.hashCode() else 0
        result = 31 * result + if (url != null) url!!.hashCode() else 0
        return result
    }

    override fun toString(): String {
        return "YtFile{" +
                "format=" + format +
                ", url='" + url + '\''.toString() +
                '}'.toString()
    }
}