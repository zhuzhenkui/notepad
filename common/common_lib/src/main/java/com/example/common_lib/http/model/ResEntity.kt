package com.example.common_lib.http.model

data class ResEntity<T>(val code: Int,
                        val msg: String,
                        val data: T)