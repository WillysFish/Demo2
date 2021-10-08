package com.willy.interviewdemo2.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.willy.interviewdemo2.utils.MLog

/**
 * Created by Willy on 2020/04/14.
 */
abstract class BaseFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MLog.sys("[%s] Lifecycle: onViewCreated()", javaClass.simpleName)
    }

    override fun onStart() {
        super.onStart()
        MLog.sys("[%s] Lifecycle: onStart()", javaClass.simpleName)
    }

    override fun onResume() {
        super.onResume()
        MLog.sys("[%s] Lifecycle: onResume()", javaClass.simpleName)
    }

    override fun onPause() {
        super.onPause()
        MLog.sys("[%s] Lifecycle: onPause()", javaClass.simpleName)
    }

    override fun onStop() {
        super.onStop()
        MLog.sys("[%s] Lifecycle: onStop()", javaClass.simpleName)
    }

    override fun onDestroy() {
        super.onDestroy()
        MLog.sys("[%s] Lifecycle: onDestroy()", javaClass.simpleName)
    }

    // 我的 Blog 文章： Snackbar: http://0rz.tw/dSHMT
    fun showSnackbar(msg: String) = Snackbar.make(requireView(), msg, Snackbar.LENGTH_SHORT).show()
}
