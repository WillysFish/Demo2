package com.willy.interviewdemo2.base

import android.app.Application
import com.lufu.koudailove.utils.SettingPrefs
import com.willy.interviewdemo2.di.AppModule
import com.willy.interviewdemo2.di.NetworkModule
import com.willy.interviewdemo2.utils.AESUtil
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level


/**
 * Created by Willy Yan on 2020/05/07.
 * Android Application class. Used for accessing singletons.
 */
class BaseApp : Application() {

    companion object {
        private lateinit var appSelf: BaseApp

        // 本地存檔
        val settingPrefs: SettingPrefs by lazy { SettingPrefs.getInstance(appSelf) }
    }

    override fun onCreate() {
        super.onCreate()
        appSelf = this
        // 初始 AES 加密的種子
        AESUtil.init(baseContext)

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(applicationContext)
            modules(listOf(AppModule, NetworkModule))
        }
    }

}