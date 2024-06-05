package com.shengdan.base_lib.base

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.common_lib.http.RetrofitManager

import com.example.common_lib.http.exception.ApiException
import com.example.common_lib.http.model.ResEntity
import com.shengdan.base_lib.entity.ErrorEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

open class BaseViewModel(application: Application) : AndroidViewModel(application) {
    protected var TAG: String = javaClass.simpleName

    /**
     * error data
     */
    val errorData: MutableLiveData<ErrorEntity> = MutableLiveData<ErrorEntity>()

    /**
     * load data
     */
    val loadData: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    /**
     * flow处理结果
     */
    suspend fun <T> Flow<T>.next(bloc: suspend T.() -> Unit): Unit = catch { }.collect { bloc(it) }

    /**
     * 通用网络请求flow 默认吐司异常处理
     */
    suspend fun <T> requestFlow(
        showLoading: Boolean = true,
        request: suspend () -> ResEntity<T>?
    ): Flow<ResEntity<T>> {
        if (showLoading) {
//            showLoading()
        }
        return flow {
            val response =  request.invoke()
//            val response = request(RetrofitManager.INSTANCE.get(BaseApiService::class.java))
                ?: throw IllegalArgumentException("数据非法，获取响应数据为空")
            if (response.code != 0) {
                throw  ApiException(response.code, response.msg)
            }
            emit(response)
        }.flowOn(Dispatchers.IO)
            .onCompletion { cause ->
                run {
                    cause?.let {
                        errorData.value = ErrorEntity(-1, "请求出错")
                        Log.e(TAG, "onCompletion==>cause: ${cause}")
                    }
                }
            }
    }

    /**
     * 通用网络请求flow 自定义异常处理
     */
    suspend fun <T> requestFlowWithError(
        showLoading: Boolean = true,
        request: suspend BaseApiService.() -> ResEntity<T>?
    ): Flow<ResEntity<T>> {
        if (showLoading) {
//            showLoading()
        }
        return flow {
            val response = request(RetrofitManager.INSTANCE.get(BaseApiService::class.java))
                ?: throw IllegalArgumentException("数据非法，获取响应数据为空")
            if (response.code != 0) {
                throw  ApiException(response.code, response.msg ?: "")
            }
            emit(response)
        }.flowOn(Dispatchers.IO)
    }
}