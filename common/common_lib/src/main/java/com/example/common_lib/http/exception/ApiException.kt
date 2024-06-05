package com.example.common_lib.http.exception

class ApiException(val code:Int,message: String?) : Exception(message) {
}