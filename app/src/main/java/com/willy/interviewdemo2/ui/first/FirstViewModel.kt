package com.willy.interviewdemo2.ui.first

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.willy.interviewdemo2.base.BaseViewModel
import com.willy.interviewdemo2.data.api.model.User
import com.willy.interviewdemo2.data.api.repo.ApiRepository
import java.util.*

class FirstViewModel(private val apiRepository: ApiRepository) : BaseViewModel() {

    // 我的 Blog 文章： Memory Leak of LiveData: http://0rz.tw/HmSWx
    private val _usersLiveData = MediatorLiveData<PagingData<User>>()
    val usersLiveData: LiveData<PagingData<User>> = _usersLiveData

    // 管理堆
    private val manageStack: Stack<LiveData<PagingData<User>>> by lazy { Stack() }


    /**
     * 搜尋用戶
     */
    fun searchUser(keyword: String) {
        // 清空管理堆
        while (manageStack.isNotEmpty()) {
            _usersLiveData.removeSource(manageStack.pop())
        }

        // 取得 Search LiveData
        val liveData = Pager(PagingConfig(pageSize = 30, initialLoadSize = 30)) {
            UserPagingSource(keyword)
        }.liveData.cachedIn(viewModelScope)

        // 加入管理堆
        manageStack.push(liveData)
        _usersLiveData.addSource(manageStack.peek()) {
            _usersLiveData.postValue(it)
        }
    }


//    /**
//     * 併發 User Api
//     */
//    private fun getUsers(loginList: List<String>): List<User> {
//        val result = mutableListOf<User>()
//        io("getUsers") {
//            loginList.map {
//                async { ApiRepoFactory.apiRepository.getUser(it) }
//            }.forEach { deferred ->
//                deferred.await().process({ result.add(it) }, { Log.sys(it.msg) })
//            }
//        }
//        return result
//    }
}
