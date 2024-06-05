package com.example.common_lib.http.converter

import android.util.Log
import com.example.common_lib.http.utils.JsonUtil
import com.example.common_lib.http.model.ResEntity
import okhttp3.ResponseBody
import retrofit2.Converter
import java.lang.reflect.Type

class GsonResponseBodyConverter<T>(
    private val type: Type
) : Converter<ResponseBody, T> {

    override fun convert(value: ResponseBody): T {
        val response = value.string()
        Log.d("GsonResponse", "convert: "+response)
        val resEntity = JsonUtil.json2Object(response, ResEntity::class.java)
        // 这里是定义成code 200为正常，不正常则抛出之前定义好的异常，在自定义的协程异常处理类中处理
        return  JsonUtil.json2Object(response, type)

    }
}