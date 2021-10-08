package com.willy.interviewdemo2.data.api.service

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.*

interface ApiService {

    @GET("/users/{login}")
    suspend fun user(
        @HeaderMap header: HashMap<String, String>,
        @Path("login") login: String,
    ): JsonObject

    @GET("/search/users")
    suspend fun searchUsers(
        @HeaderMap header: HashMap<String, String>,
        @Query("q") q: String,
        @Query("sort") sort: String? = null,
        @Query("order") order: String? = null,
        @Query("per_page") perPage: Int? = null,
        @Query("page") page: Int? = null
    ): JsonObject
}