package com.shengdan.base_lib.utils_db

import android.net.Uri
import android.text.TextUtils
import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.shengdan.base_lib.utils.ImageLoaderUtil
import java.io.File

/**
 * 封装的DataBinding视图绑定工具类
 */
object ImageViewDB {


    /**
     * 本地地图
     * @param imageView
     * @param localUrl
     */
    @JvmStatic
    @BindingAdapter("localUrl")
    fun loadImageByLocal(imageView: ImageView, localUrl: String?) {
        if (TextUtils.isEmpty(localUrl)) return
        imageView.setImageURI(Uri.fromFile(File(localUrl)))
    }

    /**
     * 本地资源图
     * @param imageView
     * @param networkUrl
     */
    @JvmStatic
    @BindingAdapter("resUrl")
    fun loadImage(imageView: ImageView, resUrl: Int) {
        ImageLoaderUtil.showLocalResImg(imageView, resUrl)
    }

    /**
     * 网络资源图
     * @param imageView
     * @param networkUrl
     */
    @JvmStatic
    @BindingAdapter("networkUrl")
    fun loadImage(imageView: ImageView, networkUrl: String?) {
        ImageLoaderUtil.showImg(imageView, "" + networkUrl)
//        ImageLoaderUtil.showImg(imageView, ApiRequest.FILE_URL + url, errorDrawable)

    }

    /**
     * 网络资源图
     * 圆角radius
     * @param imageView ImageView
     * @param networkUrl
     */
    @JvmStatic
    @BindingAdapter("networkUrl", "radius")
    fun loadImage(imageView: ImageView, networkUrl: String?, radius: Int) {
        ImageLoaderUtil.showImgRound(imageView, networkUrl, radius.toFloat())
//        ImageLoaderUtil.showImgRounds(imageView, ApiRequest.FILE_URL + networkUrl, radius.toFloat())
    }
}