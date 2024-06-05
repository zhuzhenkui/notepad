package com.shengdan.base_lib.base

import com.example.common_lib.http.RetrofitManager
import com.example.common_lib.http.model.ResEntity
import retrofit2.http.GET

/**
 * Retrofit API接口基类
 * 放置一些公共接口，如登录
 * 可供各个组件获取token
 */
interface BaseApiService {

    //登录
    @GET("api/login")
    suspend fun login(): ResEntity<Any>
}
//val Api: com.example.kotlin_study.api.ApiService by lazy {
//    RetrofitManager.INSTANCE.retrofit.create(com.example.kotlin_study.api.ApiService::class.java)
//}

//fun <S> getApiRequest(service: Class<S>?): S {
//    kotlin.synchronized(service!!.classes){
//        return RetrofitManager.INSTANCE.get(service)
//    }
//}