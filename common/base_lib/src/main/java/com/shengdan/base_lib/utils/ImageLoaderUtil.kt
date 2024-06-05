package com.shengdan.base_lib.utils

import android.content.Context
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.common_lib.glide.GlideRoundTransform
import com.shengdan.base_lib.R

object  ImageLoaderUtil {

    /**
     * 展示本地资源图片
     */
    fun showLocalResImg(imgView: ImageView, url: Int) {
        imgView.setImageResource(url)
    }

    /**
     * 展示图片
     */
    fun showImg(imgView: ImageView, url: String?) {
        Glide.with(imgView).load(url).placeholder(R.color.default_placeholder_color).into(imgView)
    }

    /**
     * 展示图片
     * 自定义展位图
     */
    fun showImg(imgView: ImageView, url: String?,placeholder:Int) {
        Glide.with(imgView).load(url).placeholder(placeholder).into(imgView)
    }

    /***
     * 展示圆角图
     * @param imgView
     * @param url
     */
    fun showImgRound(imgView: ImageView, url: String?, radius: Float) {
        Glide.with(imgView.context).load(url).placeholder(R.drawable.bg_toast_black)
            .transform(GlideRoundTransform(imgView.context, radius)).into(imgView)
    }

    /***
     * 展示圆角图
     * 自定义展位图
     */
    fun showImgRound(imgView: ImageView, url: String?, radius: Float,placeholder:Int) {
        Glide.with(imgView.context).load(url).placeholder(placeholder)
            .transform(GlideRoundTransform(imgView.context, radius)).into(imgView)
    }
}