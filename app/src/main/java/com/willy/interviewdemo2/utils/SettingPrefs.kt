package com.lufu.koudailove.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.willy.interviewdemo2.utils.AESUtil

/**
 * Created by Willy on 2020/04/14.
 */
class SettingPrefs private constructor(context: Context) {

    companion object {

        // 上次搜尋記錄
        const val SET_KEY_KEYWORD = "SET_KEY_KEYWORD"

        // For Singleton instantiation
        @Volatile
        private var instance: SettingPrefs? = null

        fun getInstance(context: Context): SettingPrefs {
            return synchronized(this) {
                instance ?: SettingPrefs(context).also { instance = it }
            }
        }
    }

    private val prefs: SharedPreferences = context.getSharedPreferences("setting", MODE_PRIVATE)

    inline fun <reified T> setObject(keyName: String, value: T) =
        setString(keyName, Gson().toJson(value))

    /**
     *  我的 Blog 文章 Kotlin 泛型: http://0rz.tw/h7vuB
     */
    inline fun <reified T> getObject(keyName: String, defaultFormat: String = "{}"): T =
        Gson().fromJson(getString(keyName, defaultFormat), T::class.java)

    inline fun <reified T> getObjectOrNull(keyName: String): T? {
        val str = getString(keyName)
        if (str.length < 3) return null
        return Gson().fromJson(str, T::class.java)
    }

    /**
     *  更新 Object 的值
     */
    inline fun <reified T> updateObject(keyName: String, update: (obj: T) -> T) =
        setObject(keyName, update(getObject(keyName)))

    /**
     *  刪除 Object
     */
    fun deleteObject(keyName: String) =
        setString(keyName, "{}")

    fun setString(keyName: String, value: String) {
        val encryptValue = AESUtil.encrypt(value)
        prefs.edit().putString(keyName, encryptValue).apply()
    }

    fun getString(keyName: String, default: String = ""): String {
        var uid = prefs.getString(keyName, default) ?: default
        if (uid == default) return uid
        uid = AESUtil.decrypt(uid)
        return uid
    }

    fun setBoolean(keyName: String, value: Boolean) {
        prefs.edit().putBoolean(keyName, value).apply()
    }

    fun getBoolean(keyName: String, default: Boolean = false): Boolean {
        return prefs.getBoolean(keyName, default)
    }

    fun setInt(keyName: String, value: Int) {
        prefs.edit().putInt(keyName, value).apply()
    }

    fun getInt(keyName: String, default: Int = 0): Int {
        return prefs.getInt(keyName, default)
    }

    fun setLong(keyName: String, value: Long) {
        prefs.edit().putLong(keyName, value).apply()
    }

    fun getLong(keyName: String, default: Long = 0): Long {
        return prefs.getLong(keyName, default)
    }

    fun removeData(keyName: String) {
        prefs.edit().remove(keyName).apply()
    }
}
