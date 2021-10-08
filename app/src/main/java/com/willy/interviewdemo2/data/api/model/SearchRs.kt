package com.willy.interviewdemo2.data.api.model


import com.google.gson.annotations.SerializedName

data class SearchRs(
    @SerializedName("incomplete_results")
    val incompleteResults: Boolean = false,
    @SerializedName("items")
    val items: List<User> = listOf(),
    @SerializedName("total_count")
    val totalCount: Int = 0
)