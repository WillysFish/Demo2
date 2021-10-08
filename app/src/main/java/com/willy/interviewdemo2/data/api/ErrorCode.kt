package com.willy.interviewdemo2.data.api

import android.os.NetworkOnMainThreadException
import com.google.gson.JsonParseException
import com.willy.interviewdemo2.utils.MLog
import retrofit2.HttpException


/**
 * Created by Willy on 2020/04/14.
 * 將會遇到的 Error 集中在這管理
 * 包含 Exception & API errorCode
 */
enum class ErrorCode(val code: Int, val msg: String) {

    NOT_MODIFIED(304, "Not Modified"),
    API_RATE_LIMIT(403, "API Rate Limit"),
    UNPROCESSABLE_ENTITY(422, "Unprocessable Entity"),
    SERVICE_UNAVAILABLE(503, "Service Unavailable"),

    // Custom Error
    JSON_PARSE_EXCEPTION(8001, "JsonParseException"),
    NETWORK_ON_MAIN_THREAD(8002, "NetworkOnMainThreadException"),

    UNKNOWN(-1, "Try Latter"),
    NO_ERROR(0, "No Error");


    companion object {

        private val map = values().associateBy(ErrorCode::code)

        @JvmStatic
        fun findErrorByThrowable(e: Throwable): ErrorCode {
            MLog.e(e, "ErrorCode")
            return when (e) {
                is JsonParseException -> JSON_PARSE_EXCEPTION
                is NetworkOnMainThreadException -> NETWORK_ON_MAIN_THREAD
                is HttpException -> map[e.code()] ?: UNKNOWN
                else -> UNKNOWN
            }
        }
    }
}