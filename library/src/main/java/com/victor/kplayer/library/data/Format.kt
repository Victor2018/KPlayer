package com.victor.kplayer.library.data

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by longtv, All rights reserved.
 * -----------------------------------------------------------------
 * File: Format.java
 * Author: Victor
 * Date: 2018/10/23 15:51
 * Description: 
 * -----------------------------------------------------------------
 */
class Format {
    enum class VCodec {
        H263, H264, MPEG4, VP8, VP9, NONE
    }

    enum class ACodec {
        MP3, AAC, VORBIS, OPUS, NONE
    }

    private var itag: Int = 0
    private var ext: String? = null
    private var height: Int = 0
    private var fps: Int = 0
    private val vCodec: VCodec? = null
    private val aCodec: ACodec? = null
    private var audioBitrate: Int = 0
    private var isDashContainer: Boolean = false
    private var isHlsContent: Boolean = false

    constructor(tag: Int, ext: String, height: Int, vCodec: VCodec, aCodec: ACodec, isDashContainer: Boolean) {
        this.itag = itag
        this.ext = ext
        this.height = height
        this.fps = 30
        this.audioBitrate = -1
        this.isDashContainer = isDashContainer
        this.isHlsContent = false
    }
    constructor(itag: Int, ext: String, vCodec: VCodec, aCodec: ACodec, audioBitrate: Int, isDashContainer: Boolean) {
        this.itag = itag
        this.ext = ext
        this.height = -1
        this.fps = 30
        this.audioBitrate = audioBitrate
        this.isDashContainer = isDashContainer
        this.isHlsContent = false
    }
    constructor(itag: Int, ext: String, height: Int, vCodec: VCodec, aCodec: ACodec, audioBitrate: Int,
                isDashContainer: Boolean) {
        this.itag = itag
        this.ext = ext
        this.height = height
        this.fps = 30
        this.audioBitrate = audioBitrate
        this.isDashContainer = isDashContainer
        this.isHlsContent = false
    }
    constructor(itag: Int, ext: String, height: Int, vCodec: VCodec, aCodec: ACodec, audioBitrate: Int,
                isDashContainer: Boolean, isHlsContent: Boolean) {
        this.itag = itag
        this.ext = ext
        this.height = height
        this.fps = 30
        this.audioBitrate = audioBitrate
        this.isDashContainer = isDashContainer
        this.isHlsContent = isHlsContent
    }

    constructor (itag: Int, ext: String, height: Int, vCodec: VCodec, fps: Int, aCodec: ACodec, isDashContainer: Boolean) {
        this.itag = itag
        this.ext = ext
        this.height = height
        this.audioBitrate = -1
        this.fps = fps
        this.isDashContainer = isDashContainer
        this.isHlsContent = false
    }

    /**
     * Get the frames per second
     */
    fun getFps(): Int {
        return fps
    }

    /**
     * Audio bitrate in kbit/s or -1 if there is no audio track.
     */
    fun getAudioBitrate(): Int {
        return audioBitrate
    }

    /**
     * An identifier used by com.victor.test.youtube for different formats.
     */
    fun getItag(): Int {
        return itag
    }

    /**
     * The file extension and conainer format like "mp4"
     */
    fun getExt(): String? {
        return ext
    }

    fun isDashContainer(): Boolean {
        return isDashContainer
    }

    fun getAudioCodec(): ACodec? {
        return aCodec
    }

    fun getVideoCodec(): VCodec? {
        return vCodec
    }

    fun isHlsContent(): Boolean {
        return isHlsContent
    }

    /**
     * The pixel height of the video stream or -1 for audio files.
     */
    fun getHeight(): Int {
        return height
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false

        val format = o as Format?

        if (itag != format!!.itag) return false
        if (height != format.height) return false
        if (fps != format.fps) return false
        if (audioBitrate != format.audioBitrate) return false
        if (isDashContainer != format.isDashContainer) return false
        if (isHlsContent != format.isHlsContent) return false
        if (if (ext != null) ext != format.ext else format.ext != null) return false
        return if (vCodec != format.vCodec) false else aCodec == format.aCodec

    }

    override fun hashCode(): Int {
        var result = itag
        result = 31 * result + if (ext != null) ext!!.hashCode() else 0
        result = 31 * result + height
        result = 31 * result + fps
        result = 31 * result + (vCodec?.hashCode() ?: 0)
        result = 31 * result + (aCodec?.hashCode() ?: 0)
        result = 31 * result + audioBitrate
        result = 31 * result + if (isDashContainer) 1 else 0
        result = 31 * result + if (isHlsContent) 1 else 0
        return result
    }

    override fun toString(): String {
        return "Format{" +
                "itag=" + itag +
                ", ext='" + ext + '\''.toString() +
                ", height=" + height +
                ", fps=" + fps +
                ", vCodec=" + vCodec +
                ", aCodec=" + aCodec +
                ", audioBitrate=" + audioBitrate +
                ", isDashContainer=" + isDashContainer +
                ", isHlsContent=" + isHlsContent +
                '}'.toString()
    }
}