package com.willy.interviewdemo2.data.api

import com.google.gson.JsonObject

/**
 * Created by Willy on 2020/06/05.
 */
sealed class ResultCenter<out T : Any> {

    data class Success<out T : Any>(val data: T) : ResultCenter<T>()

    data class Error(val throwable: Throwable) :
        ResultCenter<Nothing>() {
        val errCode = ErrorCode.findErrorByThrowable(throwable)
    }

    fun process(
        success: (data: T) -> Unit = {},
        failed: (errCode: ErrorCode) -> Unit = {},
        final: () -> Unit = {}
    ) {
        when (this) {
            is Success -> success(data)
            is Error -> failed(errCode)
        }
        final()
    }
}