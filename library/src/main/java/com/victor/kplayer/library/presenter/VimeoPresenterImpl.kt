package com.victor.kplayer.library.presenter

import com.victor.kplayer.library.data.VimeoReq
import com.victor.kplayer.library.util.VimeoParser
import com.victor.kplayer.library.view.VimeoView
import org.victor.khttp.library.annotation.HttpParms
import org.victor.khttp.library.data.Request
import org.victor.khttp.library.inject.HttpInject
import org.victor.khttp.library.presenter.impl.BasePresenterImpl

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by longtv, All rights reserved.
 * -----------------------------------------------------------------
 * File: VimeoPresenterImpl.java
 * Author: Victor
 * Date: 2018/10/24 14:31
 * Description: 
 * -----------------------------------------------------------------
 */
class VimeoPresenterImpl(var vimeoView: VimeoView?): BasePresenterImpl() {
    override fun onComplete(data: Any?, msg: String) {
        var vimeoReq = VimeoParser.parseVimeo(data?.toString())
        vimeoView?.OnVimeo(vimeoReq,msg)
    }

    override fun detachView() {
        vimeoView = null
    }

    @HttpParms (method = Request.GET,responseCls = String::class)
    override fun sendRequest(url: String, header: HashMap<String, String>?, parms: String?) {
        HttpInject.inject(this);
        super.sendRequest(url, header, parms)
    }
}