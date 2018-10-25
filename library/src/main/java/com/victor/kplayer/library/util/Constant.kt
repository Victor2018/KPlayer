package com.victor.kplayer.library.util

import java.util.HashMap

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: Constant.kt
 * Author: Victor
 * Date: 2018/8/22 15:54
 * Description: 常量实体
 * -----------------------------------------------------------------
 */
class Constant {
    companion object {
        const val VIMEO_CONFIG_URL = "https://player.vimeo.com/video/%s/config"

        const val YOUTUBE_URL = "http://www.youtube.com/get_video_info?video_id=%s&asv=3&el=detailpage&hl=en_US"
        const val WATCHV_HTTPS = "https://www.youtube.com/watch?v=%s"
        const val DECIPHER_URL = "https://s.ytimg.com/yts/jsbin/%s"
        const val PARAMETER_SEPARATOR = "&"
        const val NAME_VALUE_SEPARATOR = "="
        const val UEFSM = "url_encoded_fmt_stream_map"
        const val AF = "adaptive_fmts"

        const val YOUTUBE_HOST = "www.youtube.com"
        const val YOUTUBE_HOST2 = "youtu.be"

        const val VIMEO_URL = "https://vimeo.com/%s"
        const val VIMEO_HOST = "vimeo.com"
        const val FACEBOOK_HOST = "www.facebook.com"

        fun getVimeoHttpHeaderParm(identifier: String?): HashMap<String, String> {
            val header = HashMap<String, String>()
            header["Content-Type"] = "application/json"
            header["Referer"] = String.format(VIMEO_URL, identifier)
            return header
        }

    }

    class VideoType {
        companion object {
            const val YOUTUBE = 0x601
            const val VIMEO = 0x602
            const val M3U8  = 0x603
            const val FACEBOOK = 0x604
            const val YOUTUBE_CHECK = 0x605
        }
    }

}