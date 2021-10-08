package com.willy.interviewdemo2.ui.first

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.willy.interviewdemo2.data.api.model.User
import com.willy.interviewdemo2.databinding.ItemUserBinding


class UsersAdapter : PagingDataAdapter<User, UsersAdapter.UserVH>(UserDiff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersAdapter.UserVH =
        UserVH(
            ItemUserBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: UsersAdapter.UserVH, position: Int) {
        getItem(position)?.also { (holder).bind(it) }
    }

    inner class UserVH(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User) {
            binding.apply {
                Glide.with(root.context)
                    .load(user.avatarUrl)
                    .apply(RequestOptions.bitmapTransform(CircleCrop()))
                    .into(itemUserAvatarImg)

                itemUserNameTv.text = user.login
            }
        }
    }
}
