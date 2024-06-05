package com.example.common_lib.http

import android.util.Log
import com.example.common_lib.http.converter.GsonResponseConverterFactory
import com.example.common_lib.http.interceptor.LoggerInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.*
import java.util.concurrent.TimeUnit

class RetrofitManager private constructor() {
    //不同的业务组件存储不同的Service实例
    val retrofitMap: HashMap<String, Any> = HashMap<String, Any>()

    //初始化默认okhttpClient
    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .addInterceptor(LoggerInterceptor())
        .build()

    var retrofit: Retrofit? = null

    /**
     * 初始化必要对象和参数
     *
     * @param baseUrl    域名
     */
    fun init(baseUrl: String?) {
        // 初始化Retrofit
         retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonResponseConverterFactory)
            .client(okHttpClient)
            .build()
    }

    fun <S> get(service: Class<S>?): S {
        if (retrofitMap.containsKey(service?.name)) {
            return retrofitMap.get(service?.name) as S
        }

        val s: S = retrofit?.create(service)!!
       retrofitMap[service?.name.toString()] = s!!
        return s;
    }

    companion object {
        val INSTANCE: RetrofitManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            RetrofitManager()
        }
    }

}
