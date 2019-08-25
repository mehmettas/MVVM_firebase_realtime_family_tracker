package com.mehmettas.cent.data.remote.network

import java.lang.Exception

sealed class ResultWrapper<out T : Any> {
    class Success<out T : Any>(val data: T) : ResultWrapper<T>()
    class Error(val exception: Exception) : ResultWrapper<Nothing>()
}
