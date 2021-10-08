package com.willy.interviewdemo2.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.willy.interviewdemo2.utils.MLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Created by Willy on 2020/04/14.
 */
// 我的 Blog 文章： MVC、MVP、MVVM: http://0rz.tw/MJqbg
abstract class BaseViewModel() : ViewModel() {

    /**
     * 在 IO Thread 做事，有 Exception 額外處理
     */
    fun io(
        apiTask: suspend CoroutineScope.() -> Unit,
        errorCatch: suspend (e: Throwable) -> Unit = { MLog.e(it.toString()) }
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                apiTask()
            } catch (e: Throwable) {
                errorCatch(e)
            }
        }
    }

    /**
     * 在 IO Thread 做事，Error 只要印 log with tag
     */
    fun io(
        tag: String,
        task: suspend CoroutineScope.() -> Unit
    ) {
        io(task, { MLog.e(it, tag) })
    }

    override fun onCleared() {
        super.onCleared()
        MLog.sys("[%s] Lifecycle: onCleared()", javaClass.simpleName)
    }
}