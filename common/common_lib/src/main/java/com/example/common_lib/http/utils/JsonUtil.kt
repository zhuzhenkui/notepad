package com.example.common_lib.http.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*

object JsonUtil {

    val gson: Gson = Gson()

    fun <T> object2Json(obj: T): String = gson.toJson(obj)

    fun <T> json2Object(json: String, obj: Type): T = gson.fromJson(json, obj)

    fun <T> json2Object(json: String, obj: Class<T>): T = gson.fromJson(json, obj)

    fun <T> json2List(json: String): List<T> {
        return gson.fromJson(json, object : TypeToken<LinkedList<T>>() {}.type)
    }

    fun <T> list2Json(list: List<T>): String {
        return object2Json(list)
    }
}