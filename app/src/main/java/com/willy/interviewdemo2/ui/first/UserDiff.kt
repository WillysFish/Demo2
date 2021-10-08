package com.willy.interviewdemo2.ui.first

import androidx.recyclerview.widget.DiffUtil
import com.willy.interviewdemo2.data.api.model.User

class UserDiff : DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean = oldItem == newItem
}