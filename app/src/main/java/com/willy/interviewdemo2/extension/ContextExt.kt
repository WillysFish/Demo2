package com.willy.interviewdemo2.extension

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager

/**
 * Created by Willy on 2020/04/14.
 */

/**
 * 將 DP 轉為 PX
 */
fun Context.dpToPx(dp: Int): Int = (dp * resources.displayMetrics.density).toInt()

/**
 * 將 PX 轉為 DP
 */
fun Context.pxToDp(px: Int): Int = (px / resources.displayMetrics.density).toInt()

/**
 * 彈出軟鍵盤
 */
fun Context.popKeyboard(view: View) {
    view.requestFocus()
    val inputManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
}

/**
 * 收起軟鍵盤
 */
fun Context.hideKeyboard(view: View) {
    val inputManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
}