package com.victor.kplayer

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.alibaba.fastjson.JSON
import com.victor.kplayer.library.data.FacebookReq
import com.victor.kplayer.library.data.VimeoReq
import com.victor.kplayer.library.data.YoutubeReq
import com.victor.kplayer.library.interfaces.OnExtractListener
import com.victor.kplayer.library.module.PlayHelper
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),OnExtractListener {
    override fun OnFacebook(facebookReq: FacebookReq?) {
        Toast.makeText(applicationContext,JSON.toJSONString(facebookReq),Toast.LENGTH_SHORT).show()
    }

    override fun OnVimeo(vimeoReq: VimeoReq?) {
        Toast.makeText(applicationContext,JSON.toJSONString(vimeoReq),Toast.LENGTH_SHORT).show()
    }

    override fun OnYoutube(youtubeReq: YoutubeReq?) {
        Toast.makeText(applicationContext,JSON.toJSONString(youtubeReq),Toast.LENGTH_SHORT).show()
    }

    var playHelper: PlayHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initialize()
    }

    fun initialize () {
        playHelper = PlayHelper(this,mSvPlay,null)
//        playHelper?.play("SMcXGeltEQQ",this)//youtube video
//        playHelper?.play("ozv4q2ov3Mk",this)//youtube video
//        playHelper?.play("yk2CUjbyyQY",this)//youtube live
//        playHelper?.play("https://vimeo.com/channels/staffpicks/262705319",this)//vimeo
//        playHelper?.play("https://www.facebook.com/1541202502800731/videos/1995585847362392/",this)//facebook
        playHelper?.play("http://ivi.bupt.edu.cn/hls/cctv3.m3u8",null)//m3u8
    }

    override fun onResume() {
        super.onResume()
        playHelper?.onResume()
    }

    override fun onPause() {
        super.onPause()
        playHelper?.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        playHelper?.onDestroy()
        playHelper = null
    }
}
