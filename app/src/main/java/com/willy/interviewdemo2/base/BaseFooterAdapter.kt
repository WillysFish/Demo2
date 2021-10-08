package com.willy.interviewdemo2.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.willy.interviewdemo2.R
import com.willy.interviewdemo2.data.api.ErrorCode
import com.willy.interviewdemo2.databinding.ItemFooterBinding
import com.willy.interviewdemo2.utils.TimerUtil

class BaseFooterAdapter(private val retryCallback: (view: View) -> Unit) :
    LoadStateAdapter<BaseFooterAdapter.FooterVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState) = FooterVH(
        ItemFooterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ),
        retryCallback
    )

    override fun onBindViewHolder(holder: FooterVH, loadState: LoadState) {
        holder.bind(loadState)
    }

    inner class FooterVH(
        private val binding: ItemFooterBinding,
        retryCallback: (view: View) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.retryBtn.setOnClickListener(retryCallback)
        }

        fun bind(loadState: LoadState) {
            binding.apply {
                loading.visibility = if (loadState is LoadState.Loading) View.VISIBLE else View.GONE

                if (loadState is LoadState.Error) {
                    retryBtn.isEnabled = false
                    retryBtn.visibility = View.VISIBLE
                    errorMsgTv.visibility = View.VISIBLE
                    errorMsgTv.text = ErrorCode.findErrorByThrowable(loadState.error).msg

                    // 30 秒後開放 Error Retry
                    TimerUtil.countDownTask(30, 1000, {
                        retryBtn.text = it.toString()
                    }) {
                        retryBtn.isEnabled = true
                        retryBtn.text = root.context.getString(R.string.retry)
                    }
                } else {
                    retryBtn.visibility = View.GONE
                    errorMsgTv.visibility = View.GONE
                }
            }
        }
    }
}