package com.victor.kplayer.library.module.jsevaluator

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by longtv, All rights reserved.
 * -----------------------------------------------------------------
 * File: JsFunctionCallFormatter.java
 * Author: Victor
 * Date: 2018/10/23 17:08
 * Description: 
 * -----------------------------------------------------------------
 */
class JsFunctionCallFormatter {
    companion object {

        fun paramToString(param: Any): String {
            var str = ""
            if (param is String) {
                str = param
                str = str.replace("\\", "\\\\")
                str = str.replace("\"", "\\\"")
                str = str.replace("\n", "\\n")
                str = String.format("\"%s\"", str)
            } else {
                try {
                    val d = java.lang.Double.parseDouble(param.toString())
                    str = param.toString()
                } catch (nfe: NumberFormatException) {
                }

            }

            return str
        }

        fun toString(functionName: String, vararg args: Any): String {
            val paramsStr = StringBuilder()

            for (param in args) {
                if (paramsStr.length > 0) {
                    paramsStr.append(", ")
                }

                paramsStr.append(paramToString(param))
            }

            return String.format("%s(%s)", functionName, paramsStr)
        }
    }
}