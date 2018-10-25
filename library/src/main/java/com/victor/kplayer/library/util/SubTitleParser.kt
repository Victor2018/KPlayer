package com.victor.kplayer.library.util

import android.text.TextUtils
import com.victor.kplayer.library.data.SubTitleInfo
import com.victor.kplayer.library.data.SubTitleListInfo
import org.dom4j.Document
import org.dom4j.DocumentException
import org.dom4j.Element
import org.dom4j.io.SAXReader
import java.io.ByteArrayInputStream
import java.io.UnsupportedEncodingException
import java.util.HashMap

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by longtv, All rights reserved.
 * -----------------------------------------------------------------
 * File: SubTitleParser.java
 * Author: Victor
 * Date: 2018/10/25 14:38
 * Description: 
 * -----------------------------------------------------------------
 */
class SubTitleParser {
    companion object {
        fun parseSubTitleList(result: String): SubTitleListInfo? {
            val datas = HashMap<Int, SubTitleListInfo>()
            if (TextUtils.isEmpty(result)) return null
            var defaultLan: SubTitleListInfo? = null
            val reader = SAXReader()
            var doc: Document? = null
            try {
                doc = reader.read(ByteArrayInputStream(result.toByteArray(charset("utf-8"))))
                val root = doc!!.getRootElement()
                //            System.out.println("docid = " + root.attributeValue("docid"));
                val iterator = root.elementIterator("track")
                var key = 0
                while (iterator.hasNext()) {
                    val e: Element = iterator.next() as Element
                    val data = SubTitleListInfo()
                    data.id = Integer.parseInt(e.attributeValue("id"))
                    data.name = e.attributeValue("name")
                    data.lang_code = e.attributeValue("lang_code")
                    data.lang_original = e.attributeValue("lang_original")
                    data.lang_translated = e.attributeValue("lang_translated")
                    data.lang_default = java.lang.Boolean.parseBoolean(e.attributeValue("lang_default"))

                    if (data.lang_default) {
                        defaultLan = data
                    }
                    datas[key] = data
                    key++
                }
                if (key == 1) {
                    defaultLan = datas[0]
                }
            } catch (e: DocumentException) {
                e.printStackTrace()
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            }

            return defaultLan
        }

        fun parseSubTitle(result: String): HashMap<Int, SubTitleInfo> {
            val datas = HashMap<Int, SubTitleInfo>()
            if (TextUtils.isEmpty(result)) return datas
            val reader = SAXReader()
            try {
                val doc = reader.read(ByteArrayInputStream(result.toByteArray(charset("utf-8"))))
                val root = doc.rootElement
                val iterator = root.elementIterator("text")
                var key = 0
                while (iterator.hasNext()) {
                    val e: Element = iterator.next() as Element
                    val data = SubTitleInfo()
                    data.beginTime = (java.lang.Double.parseDouble(e.attributeValue("start")) * 1000).toInt()
                    data.endTime = data.beginTime + (java.lang.Double.parseDouble(e.attributeValue("dur")) * 1000).toInt()
                    var subTitle = e.getStringValue()
                    if (!TextUtils.isEmpty(subTitle)) {
                        subTitle = subTitle.replace("&quot;".toRegex(), "\"")
                        subTitle = subTitle.replace("&amp;".toRegex(), "&")
                        subTitle = subTitle.replace("&#39;".toRegex(), "'")
                        subTitle = subTitle.replace("&lt;".toRegex(), "<")
                        subTitle = subTitle.replace("&gt;".toRegex(), ">")
                        data.srtBody = subTitle
                    }
                    datas[key] = data
                    key++
                }
            } catch (e: DocumentException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            }
            return datas
        }
    }
}