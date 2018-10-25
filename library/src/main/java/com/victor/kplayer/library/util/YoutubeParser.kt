package com.victor.kplayer.library.util

import java.net.URLDecoder
import java.util.*
import android.text.TextUtils
import android.util.Log
import android.util.SparseArray
import com.victor.kplayer.library.data.*
import java.io.UnsupportedEncodingException
import java.util.regex.Matcher
import java.util.regex.Pattern




/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by longtv, All rights reserved.
 * -----------------------------------------------------------------
 * File: YoutubeParser.java
 * Author: Victor
 * Date: 2018/10/23 14:19
 * Description: 
 * -----------------------------------------------------------------
 */
class YoutubeParser {
    companion object {
        private val TAG = "YoutubeParser"
        private val includeWebM = true

        private val patTitle = Pattern.compile("title=(.*?)(&|\\z)")
        private val patAuthor = Pattern.compile("author=(.+?)(&|\\z)")
        private val patChannelId = Pattern.compile("ucid=(.+?)(&|\\z)")
        private val patLength = Pattern.compile("length_seconds=(\\d+?)(&|\\z)")
        private val patViewCount = Pattern.compile("view_count=(\\d+?)(&|\\z)")
        private val patStatusOk = Pattern.compile("status=ok(&|,|\\z)")

        private val patHlsvp = Pattern.compile("hlsvp=(.+?)(&|\\z)")
        private val patHlsItag = Pattern.compile("/itag/(\\d+?)/")

        val STREAM_MAP_STRING = "url_encoded_fmt_stream_map"
        private val patIsSigEnc = Pattern.compile("s%3D([0-9A-F|.]{10,}?)(%26|%2C)")
        private val patDecryptionJsFile = Pattern.compile("jsbin\\\\/(player(_ias)?-(.+?).js)")
        private val patDashManifest2 = Pattern.compile("\"dashmpd\":\"(.+?)\"")
        private val patDashManifestEncSig = Pattern.compile("/s/([0-9A-F|.]{10,}?)(/|\\z)")
        private val patItag = Pattern.compile("itag=([0-9]+?)([&,])")
        private val patEncSig = Pattern.compile("s=([0-9A-F|.]{10,}?)([&,\"])")
        private val patUrl = Pattern.compile("url=(.+?)([&,])")
        private val patSignatureDecFunction = Pattern.compile("(\\w+)\\s*=\\s*function\\((\\w+)\\).\\s*\\2=\\s*\\2\\.split\\(\"\"\\)\\s*;")
        private val patVariableFunction = Pattern.compile("([{; =])([a-zA-Z$][a-zA-Z0-9$]{0,2})\\.([a-zA-Z$][a-zA-Z0-9$]{0,2})\\(")
        private val patFunction = Pattern.compile("([{; =])([a-zA-Z\$_][a-zA-Z0-9$]{0,2})\\(")

        private var FORMAT_MAP = SparseArray<Format>()

        init {
            // Video and Audio
            FORMAT_MAP.put(17, Format(17, "3gp", 144, Format.VCodec.MPEG4, Format.ACodec.AAC, 24, false))
            FORMAT_MAP.put(36, Format(36, "3gp", 240, Format.VCodec.MPEG4, Format.ACodec.AAC, 32, false))
            FORMAT_MAP.put(5, Format(5, "flv", 240, Format.VCodec.H263, Format.ACodec.MP3, 64, false))
            FORMAT_MAP.put(43, Format(43, "webm", 360, Format.VCodec.VP8, Format.ACodec.VORBIS, 128, false))
            FORMAT_MAP.put(18, Format(18, "mp4", 360, Format.VCodec.H264, Format.ACodec.AAC, 96, false))
            FORMAT_MAP.put(22, Format(22, "mp4", 720, Format.VCodec.H264, Format.ACodec.AAC, 192, false))

            // Dash Video
            FORMAT_MAP.put(160, Format(160, "mp4", 144, Format.VCodec.H264, Format.ACodec.NONE, true))
            FORMAT_MAP.put(133, Format(133, "mp4", 240, Format.VCodec.H264, Format.ACodec.NONE, true))
            FORMAT_MAP.put(134, Format(134, "mp4", 360, Format.VCodec.H264, Format.ACodec.NONE, true))
            FORMAT_MAP.put(135, Format(135, "mp4", 480, Format.VCodec.H264, Format.ACodec.NONE, true))
            FORMAT_MAP.put(136, Format(136, "mp4", 720, Format.VCodec.H264, Format.ACodec.NONE, true))
            FORMAT_MAP.put(137, Format(137, "mp4", 1080, Format.VCodec.H264, Format.ACodec.NONE, true))
            FORMAT_MAP.put(264, Format(264, "mp4", 1440, Format.VCodec.H264, Format.ACodec.NONE, true))
            FORMAT_MAP.put(266, Format(266, "mp4", 2160, Format.VCodec.H264, Format.ACodec.NONE, true))

            FORMAT_MAP.put(298, Format(298, "mp4", 720, Format.VCodec.H264, 60, Format.ACodec.NONE, true))
            FORMAT_MAP.put(299, Format(299, "mp4", 1080, Format.VCodec.H264, 60, Format.ACodec.NONE, true))

            // Dash Audio
            FORMAT_MAP.put(140, Format(140, "m4a", Format.VCodec.NONE, Format.ACodec.AAC, 128, true))
            FORMAT_MAP.put(141, Format(141, "m4a", Format.VCodec.NONE, Format.ACodec.AAC, 256, true))

            // WEBM Dash Video
            FORMAT_MAP.put(278, Format(278, "webm", 144, Format.VCodec.VP9, Format.ACodec.NONE, true))
            FORMAT_MAP.put(242, Format(242, "webm", 240, Format.VCodec.VP9, Format.ACodec.NONE, true))
            FORMAT_MAP.put(243, Format(243, "webm", 360, Format.VCodec.VP9, Format.ACodec.NONE, true))
            FORMAT_MAP.put(244, Format(244, "webm", 480, Format.VCodec.VP9, Format.ACodec.NONE, true))
            FORMAT_MAP.put(247, Format(247, "webm", 720, Format.VCodec.VP9, Format.ACodec.NONE, true))
            FORMAT_MAP.put(248, Format(248, "webm", 1080, Format.VCodec.VP9, Format.ACodec.NONE, true))
            FORMAT_MAP.put(271, Format(271, "webm", 1440, Format.VCodec.VP9, Format.ACodec.NONE, true))
            FORMAT_MAP.put(313, Format(313, "webm", 2160, Format.VCodec.VP9, Format.ACodec.NONE, true))

            FORMAT_MAP.put(302, Format(302, "webm", 720, Format.VCodec.VP9, 60, Format.ACodec.NONE, true))
            FORMAT_MAP.put(308, Format(308, "webm", 1440, Format.VCodec.VP9, 60, Format.ACodec.NONE, true))
            FORMAT_MAP.put(303, Format(303, "webm", 1080, Format.VCodec.VP9, 60, Format.ACodec.NONE, true))
            FORMAT_MAP.put(315, Format(315, "webm", 2160, Format.VCodec.VP9, 60, Format.ACodec.NONE, true))

            // WEBM Dash Audio
            FORMAT_MAP.put(171, Format(171, "webm", Format.VCodec.NONE, Format.ACodec.VORBIS, 128, true))

            FORMAT_MAP.put(249, Format(249, "webm", Format.VCodec.NONE, Format.ACodec.OPUS, 48, true))
            FORMAT_MAP.put(250, Format(250, "webm", Format.VCodec.NONE, Format.ACodec.OPUS, 64, true))
            FORMAT_MAP.put(251, Format(251, "webm", Format.VCodec.NONE, Format.ACodec.OPUS, 160, true))

            // HLS Live Stream
            FORMAT_MAP.put(91, Format(91, "mp4", 144, Format.VCodec.H264, Format.ACodec.AAC, 48, false, true))
            FORMAT_MAP.put(92, Format(92, "mp4", 240, Format.VCodec.H264, Format.ACodec.AAC, 48, false, true))
            FORMAT_MAP.put(93, Format(93, "mp4", 360, Format.VCodec.H264, Format.ACodec.AAC, 128, false, true))
            FORMAT_MAP.put(94, Format(94, "mp4", 480, Format.VCodec.H264, Format.ACodec.AAC, 128, false, true))
            FORMAT_MAP.put(95, Format(95, "mp4", 720, Format.VCodec.H264, Format.ACodec.AAC, 256, false, true))
            FORMAT_MAP.put(96, Format(96, "mp4", 1080, Format.VCodec.H264, Format.ACodec.AAC, 256, false, true))
        }

        fun parseYoutubeDecipher(response: String): DecipherData {
            val data = DecipherData()
            var mat = patSignatureDecFunction.matcher(response)
            if (mat.find()) {
                data.decipherFunctionName = mat.group(1)
                Log.e(TAG, "Decipher Functname: " + data.decipherFunctionName)

                val patMainVariable = Pattern.compile("(var |\\s|,|;)" + data.decipherFunctionName!!.replace("$", "\\$") +
                        "(=function\\((.{1,3})\\)\\{)")

                var mainDecipherFunct: String

                mat = patMainVariable.matcher(response)
                if (mat.find()) {
                    mainDecipherFunct = "var " + data.decipherFunctionName + mat.group(2)
                } else {
                    val patMainFunction = Pattern.compile("function " + data.decipherFunctionName!!.replace("$", "\\$") +
                            "(\\((.{1,3})\\)\\{)")
                    mat = patMainFunction.matcher(response)
                    if (!mat.find()) {
                        return data
                    }
                    mainDecipherFunct = "function " + data.decipherFunctionName + mat.group(2)
                }

                var startIndex = mat.end()

                run {
                    var braces = 1
                    var i = startIndex
                    while (i < response.length) {
                        if (braces == 0 && startIndex + 5 < i) {
                            mainDecipherFunct += response.substring(startIndex, i) + ";"
                            break
                        }
                        if (response[i] == '{')
                            braces++
                        else if (response[i] == '}')
                            braces--
                        i++
                    }
                }
                data.decipherFunctions = mainDecipherFunct
                // Search the main function for extra functions and variables
                // needed for deciphering
                // Search for variables
                mat = patVariableFunction.matcher(mainDecipherFunct)
                while (mat.find()) {
                    val variableDef = "var " + mat.group(2) + "={"
                    if (data.decipherFunctions!!.contains(variableDef)) {
                        continue
                    }
                    startIndex = response.indexOf(variableDef) + variableDef.length
                    var braces = 1
                    var i = startIndex
                    while (i < response.length) {
                        if (braces == 0) {
                            data.decipherFunctions += variableDef + response.substring(startIndex, i) + ";"
                            break
                        }
                        if (response[i] == '{')
                            braces++
                        else if (response[i] == '}')
                            braces--
                        i++
                    }
                }
                // Search for functions
                mat = patFunction.matcher(mainDecipherFunct)
                while (mat.find()) {
                    val functionDef = "function " + mat.group(2) + "("
                    if (data.decipherFunctions!!.contains(functionDef)) {
                        continue
                    }
                    startIndex = response.indexOf(functionDef) + functionDef.length
                    var braces = 0
                    var i = startIndex
                    while (i < response.length) {
                        if (braces == 0 && startIndex + 5 < i) {
                            data.decipherFunctions += functionDef + response.substring(startIndex, i) + ";"
                            break
                        }
                        if (response[i] == '{')
                            braces++
                        else if (response[i] == '}')
                            braces--
                        i++
                    }
                }

                Log.e(TAG, "Decipher Function: " + data.decipherFunctions)
                //            decipherViaWebView(encSignatures);
                /*if (CACHING) {
                writeDeciperFunctToChache();
            }*/
            }
            return data
        }

        fun parseYoutubeHtml(response: String): YoutubeHtmlData {
            val data = YoutubeHtmlData()
            val encSignatures = SparseArray<String>()
            val ytFiles = SparseArray<YtFile>()
            var curJsFileName: String? = null
            var mat = patDecryptionJsFile.matcher(response)
            if (mat.find()) {
                curJsFileName = mat.group(1).replace("\\/", "/")
                if (mat.group(2) != null) {
                    curJsFileName.replace(mat.group(2), "")
                }
                data.decipherJsFileName = curJsFileName
                Log.e(TAG, "parseYoutubeHtml()......curJsFileName = $curJsFileName")
            }

            val streams = response.split(",|$STREAM_MAP_STRING|&adaptive_fmts=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            Log.e(TAG, "parseYoutubeHtml()......streams.length = " + streams.size)
            for (encStream in streams) {
//                encStream = "$encStream,"
                var streamItem = "$encStream,"
                if (!streamItem.contains("itag%3D")) {
                    continue
                }
                val stream: String
                try {
                    stream = URLDecoder.decode(streamItem, "UTF-8")
                    mat = patItag.matcher(stream)
                    val itag: Int
                    if (mat.find()) {
                        itag = Integer.parseInt(mat.group(1))
                        Log.e(TAG, "Itag found:$itag")
                        if (FORMAT_MAP.get(itag) == null) {
                            Log.e(TAG, "Itag not in list:$itag")
                            continue
                        } else if (!includeWebM && FORMAT_MAP.get(itag).getExt().equals("webm")) {
                            continue
                        }
                    } else {
                        continue
                    }

                    if (!TextUtils.isEmpty(curJsFileName)) {
                        mat = patEncSig.matcher(stream)
                        if (mat.find()) {
                            Log.e(TAG, "itag------>" + itag + "---" + mat.group(1))
                            encSignatures.append(itag, mat.group(1))
                        }
                    }

                    mat = patUrl.matcher(streamItem)
                    var url: String? = null
                    if (mat.find()) {
                        url = mat.group(1)
                    }
                    if (url != null) {
                        val format = FORMAT_MAP.get(itag)
                        val finalUrl = URLDecoder.decode(url, "UTF-8")
                        Log.e(TAG, "finalUrl------>$finalUrl")//这里url访问会403无法播放
                        val newVideo = YtFile(format, finalUrl)
                        ytFiles.put(itag, newVideo)
                    }


                } catch (e: UnsupportedEncodingException) {
                    e.printStackTrace()
                }

            }
            data.ytFiles = ytFiles
            data.encSignatures = encSignatures
            return data
        }

        fun parseYoutubeSigEnc(response: String?): Boolean {
            Log.e(TAG, "parseYoutubeHtml()......")
            // "use_cipher_signature" disappeared, we check whether at least one ciphered signature
            // exists int the stream_map.
            val mat: Matcher
            var sigEnc = true
            var statusFail = false
            if (response != null && response.contains(STREAM_MAP_STRING)) {
                val streamMapSub = response.substring(response.indexOf(STREAM_MAP_STRING))
                mat = patIsSigEnc.matcher(streamMapSub)
                if (!mat.find()) {
                    sigEnc = false
                    if (!patStatusOk.matcher(response).find()) {
                        statusFail = true
                    }
                }
            }
            // Some videos are using a ciphered signature we need to get the
            // deciphering js-file from the youtubepage.

            return sigEnc || statusFail

        }

        fun parseYoutubeData(response: String): YoutubeReq? {
            var data: YoutubeReq? = YoutubeReq()
            try {
                val videoInfoMap = getVideoInfoMap(response, "utf-8")
                var mat = patTitle.matcher(response)
                if (mat.find()) {
                    data!!.title = URLDecoder.decode(mat.group(1), "UTF-8")
                }

                var channelIdMat = patChannelId.matcher(response)
                if (channelIdMat.find()) {
                    data!!.channelId = mat.group(1)
                }

                data!!.hlsvp = videoInfoMap["hlsvp"]
                data.author = videoInfoMap["author"]
                data.videoid = videoInfoMap["video_id"]
                data.rating = videoInfoMap["avg_rating"]
                data.length = java.lang.Long.parseLong(videoInfoMap["length_seconds"])
                data.viewcount = Integer.parseInt(videoInfoMap["view_count"])
                try {
                    data.thumb = URLDecoder.decode(videoInfoMap["thumbnail_url"], "utf-8")
                } catch (e: UnsupportedEncodingException) {
                    e.printStackTrace()
                }

                // duration=//TODO:时长
                // TODO:解析视频格式,这货后面几个值是个啥
                // fmt_list=
                // 22/1280x720/9/0/115,
                // 43/640x360/99/0/0,
                // 18/640x360/9/0/115,
                // 5/320x240/7/0/0,
                // 36/320x240/99/1/0,
                // 17/176x144/99/1/0
                //            String fmtList = videoInfoMap.get("fmt_list");
                //            String[] fmtArray = fmtList.split(",");
                //            for (String fmt : fmtArray) {
                //                String[] format = fmt.split("/");
                //            }
                //            data.keywords = videoInfoMap.get("keywords").split(",");
                data.bigthumb = videoInfoMap["iurlsd"]
                data.bigthumbhd = videoInfoMap["iurlsdmaxres"]
                // ciphertag//TODO:这货标识是否使用密码签名 'use_cipher_signature': ['True']
                data.ciphertag = TextUtils.equals(videoInfoMap["use_cipher_signature"], "True")
                // 解析流列表(这货又是个啥)
                data.sm = extractStreamMap(Constant.UEFSM, videoInfoMap, TextUtils.isEmpty(data.jsurl))
                data.asm = extractStreamMap(Constant.AF, videoInfoMap, TextUtils.isEmpty(data.jsurl))
                data.have_basic = true
            } catch (e: Exception) {
                e.printStackTrace()
                data = null
            }

            return data
        }

        fun getVideoInfoMap (response: String,encoding: String) : HashMap<String, String> {
            val parameters = HashMap<String, String>()
            var scanner: Scanner = Scanner(response)
            scanner.useDelimiter(Constant.PARAMETER_SEPARATOR)
            while (scanner.hasNext()) {
                val nameValue = scanner.next().split(Constant.NAME_VALUE_SEPARATOR)
                if (nameValue.size == 0 || nameValue.size > 2) {
                    throw IllegalArgumentException("bad parameter")
                }
                val name = decode(nameValue[0], encoding)
                var value: String? = null
                if (nameValue.size == 2) {
                    value = decode(nameValue[1], encoding)
                }
                parameters.put(name, value!!)
            }
            return parameters
        }

        fun parseFmtStreamMap(scanner: Scanner, encoding: String): FmtStreamMap {
            val streamMap = FmtStreamMap()
            scanner.useDelimiter(Constant.PARAMETER_SEPARATOR)
            while (scanner.hasNext()) {
                val nameValue = scanner.next().split(Constant.NAME_VALUE_SEPARATOR)
                if (nameValue.size == 0 || nameValue.size > 2)
                    throw IllegalArgumentException("bad parameter")

                val name = decode(nameValue[0], encoding)
                var value: String? = null
                if (nameValue.size == 2)
                    value = decode(nameValue[1], encoding)

                // fallback_host=tc.v1.cache8.googlevideo.com&
                // s=9E89E8DE8FF59D59BA5F96D9A220724C1A304F634B2C19.55E8C8A3A7C02C3FBF4D274A85A41F5F55F0401B&
                // itag=17&
                // type=video%2F3gpp%3B+codecs%3D%22mp4v.20.3%2C+mp4a.40.2%22&
                // quality=small&
                // url=http%3A%2F%2Fr20---sn-a5m7lne6.googlevideo.com%2Fvideoplayback%3Fkey%3Dyt5%26ip%3D173.254.202.174%26mt%3D1393571459%26fexp%3D936112%252C937417%252C937416%252C913434%252C936910%252C936913%252C902907%26itag%3D17%26source%3Dyoutube%26sver%3D3%26mv%3Dm%26ms%3Dau%26sparams%3Dgcr%252Cid%252Cip%252Cipbits%252Citag%252Csource%252Cupn%252Cexpire%26ipbits%3D0%26expire%3D1393597755%26gcr%3Dus%26upn%3Du-4gaUCuZCM%26id%3D782b01f5511b174f

                if (TextUtils.equals("fallback_host", name)) {
                    streamMap.fallbackHost = value
                }
                if (TextUtils.equals("s", name)) {
                    streamMap.s = value
                }
                if (TextUtils.equals("itag", name)) {
                    streamMap.itag = value
                }
                if (TextUtils.equals("type", name)) {
                    streamMap.type = value
                }
                if (TextUtils.equals("quality", name)) {
                    streamMap.quality = value
                }
                if (TextUtils.equals("url", name)) {
                    streamMap.url = value
                }
                if (TextUtils.equals("sig", name)) {
                    streamMap.sig = value
                }
            }
            return streamMap
        }

        /** @param uefsm2
         * @param videoInfoMap
         * @param empty
         * //这货是做啥用的
         */
        fun extractStreamMap(uefsm2: String, videoInfoMap: HashMap<String, String>?, empty: Boolean): List<FmtStreamMap> {
            val streamMaps = ArrayList<FmtStreamMap>()
            if (videoInfoMap != null && videoInfoMap.containsKey(uefsm2)) {
                val uefms2 = videoInfoMap[uefsm2]
                if (!TextUtils.isEmpty(uefms2)) {
                    if (uefms2!!.contains(",")) {
                        val uefms2s = uefms2.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        for (s in uefms2s) {
                            val streamMap = YoutubeParser.parseFmtStreamMap(Scanner(s), "utf-8")
                            streamMaps.add(streamMap)
                        }
                    }
                }
            }
            return streamMaps
        }

        fun extractVideoId(url: String): String {
            val p = Pattern.compile("(?:^|[^\\w-]+)([\\w-]{11})(?:[^\\w-]+|$)")
            val matcher = p.matcher(url)
            // for (int i = 0; i < groupCount; i++) {
            val group = matcher.group(1)
            println(group)
            // }
            return group
        }

        private fun decode(content: String, encoding: String): String {
            try {
                return URLDecoder.decode(content,encoding)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return ""
        }
    }
}