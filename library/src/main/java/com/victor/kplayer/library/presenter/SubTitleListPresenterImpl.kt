package com.victor.kplayer.library.presenter

import com.victor.kplayer.library.view.SubTitleListView
import org.victor.khttp.library.annotation.HttpParms
import org.victor.khttp.library.data.Request
import org.victor.khttp.library.inject.HttpInject
import org.victor.khttp.library.presenter.impl.BasePresenterImpl

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: LivePresenterImpl.kt
 * Author: Victor
 * Date: 2018/8/24 13:49
 * Description: 
 * -----------------------------------------------------------------
 */
class SubTitleListPresenterImpl(var subTitleListView: SubTitleListView?): BasePresenterImpl() {

    override fun onComplete(data: Any?, msg: String) {
        subTitleListView?.OnSubTitleList(data,msg)
    }

    override fun detachView() {
        subTitleListView = null
    }

    @HttpParms (method = Request.GET,responseCls = String::class)
    override fun sendRequest(url: String, header: HashMap<String, String>?, parms: String?) {
        HttpInject.inject(this);
        super.sendRequest(url, header, parms)
    }
}