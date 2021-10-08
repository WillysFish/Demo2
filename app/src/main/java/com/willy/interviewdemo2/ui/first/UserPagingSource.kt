package com.willy.interviewdemo2.ui.first

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.willy.interviewdemo2.data.api.model.User
import com.willy.interviewdemo2.data.api.repo.ApiRepository
import org.koin.java.KoinJavaComponent.inject

class UserPagingSource(private val keyword: String) : PagingSource<Int, User>() {

    private val apiRepository: ApiRepository by inject(ApiRepository::class.java)

    override fun getRefreshKey(state: PagingState<Int, User>): Int? =
        state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
        }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> = try {
        val page = params.key ?: 1
        val rs = apiRepository.searchUsersForPaging(keyword, page)

        // 全搜到 or 搜不到時，無下一頁
        LoadResult.Page(
            rs.items,
            null,
            if (rs.incompleteResults || rs.totalCount == 0) null else page + 1
        )
    } catch (e: Exception) {
        LoadResult.Error(e)
    }
}
