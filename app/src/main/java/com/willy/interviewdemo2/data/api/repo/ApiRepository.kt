package com.willy.interviewdemo2.data.api.repo

import com.google.gson.Gson
import com.willy.interviewdemo2.data.api.ResultCenter
import com.willy.interviewdemo2.data.api.model.SearchRs
import com.willy.interviewdemo2.data.api.model.User
import com.willy.interviewdemo2.data.api.service.ApiService
import java.util.*

class ApiRepository(private val service: ApiService) {

    private val headerMap: HashMap<String, String> by lazy {
        val result = HashMap<String, String>()
        result["accept"] = "application/vnd.github.v3+json"
        result
    }

    /**
     * 取得用戶資料
     */
    suspend fun getUser(login: String): ResultCenter<User> = try {
        val rs = service.user(headerMap, login)
        ResultCenter.Success(Gson().fromJson(rs, User::class.java))
    } catch (e: Throwable) {
        ResultCenter.Error(e)
    }

    /**
     * 搜尋用戶 (分頁)
     */
    suspend fun searchUsersForPaging(
        keyword: String,
        page: Int = 1,
        perPage: Int = 30
    ): SearchRs {
        val rs =
            service.searchUsers(headerMap, keyword, perPage = perPage, page = page)
        return Gson().fromJson(rs, SearchRs::class.java)
    }

}