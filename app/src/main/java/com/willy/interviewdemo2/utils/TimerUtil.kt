package com.willy.interviewdemo2.utils

import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * Created by Willy on 2020/04/21.
 */
object TimerUtil {
    private val disposables = CompositeDisposable()
    private var infinityIntervalDisposableMap = mutableMapOf<String, Disposable>()
    private var countdownDisposableMap = mutableMapOf<String, Disposable>()

    /**
     * 停止所有任務
     * Using clear will clear all, but can accept new disposable
     * Using dispose will clear all and set isDisposed = true, so it will not accept any new disposable
     */
    fun stopAllTasks() = disposables.clear()


    /**
     * 無限重復任務 (Max Long)
     * @param periodMs 任務間隔時間(ms)
     * @param taskName 任務名稱
     * @param task 要執行的任務
     */
    fun infinityIntervalTask(periodMs: Long, taskName: String, task: (current: Long) -> Unit) {
        // 不重覆開啟同名任務
        if (infinityIntervalDisposableMap.containsKey(taskName)) return

        Flowable.intervalRange(0, Long.MAX_VALUE, 0, periodMs, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ task(it) }, { MLog.e(it, "TimerUtil Error:") })
            ?.let {
                disposables.add(it)
                infinityIntervalDisposableMap.put(taskName, it)
            }
    }

    /**
     * 停止 - 無限重復任務 (Max Long)
     * @param taskName 任務名稱
     */
    fun stopInfinityIntervalTask(taskName: String) =
        infinityIntervalDisposableMap[taskName]?.also {
            disposables.remove(it)
            infinityIntervalDisposableMap.remove(taskName)
        }

    /**
     * 停止全部 - 無限重復任務 (Max Long)
     */
    fun stopAllInfinityIntervalTask() {
        infinityIntervalDisposableMap.forEach { (_, u) -> disposables.remove(u) }
        infinityIntervalDisposableMap.clear()
    }

    /**
     * 倒數執行任務
     * @param countDownNumber 倒數啟始數字
     * @param periodMs 倒數間隔時間(ms)
     * @param relayTask 倒數期間的中繼任務
     * @param finalTask 倒數結束後的任務
     */
    fun countDownTask(
        countDownNumber: Long,
        periodMs: Long,
        relayTask: (current: Long) -> Unit,
        finalTask: () -> Unit = {}
    ): Disposable? {
        val dis = Observable.intervalRange(
            0,
            countDownNumber + 1,
            0,
            periodMs,
            TimeUnit.MILLISECONDS,
            Schedulers.io()
        ).observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                relayTask(countDownNumber - it)
                if (it == countDownNumber) finalTask()
            }, { MLog.e(it, "TimerUtil Error:") })

        disposables.add(dis)
        return dis
    }

    /**
     * 屬名的倒數執行任務
     * @param taskName 任務名稱
     * @param countDownNumber 倒數啟始數字
     * @param periodMs 倒數間隔時間(ms)
     * @param relayTask 倒數期間的中繼任務
     * @param finalTask 倒數結束後的任務
     */
    fun countDownTask(
        taskName: String,
        countDownNumber: Long,
        periodMs: Long,
        relayTask: (current: Long) -> Unit,
        finalTask: () -> Unit = {}
    ) {
        // 不重覆開啟同名任務
        if (countdownDisposableMap.containsKey(taskName)) return

        val dis = countDownTask(countDownNumber, periodMs, relayTask, finalTask)
        dis?.apply {
            countdownDisposableMap[taskName] = this
        }
    }


    /**
     * 停止 - 倒數任務
     * @param taskName 任務名稱
     */
    fun stopCountdownTask(taskName: String) =
        countdownDisposableMap[taskName]?.also {
            disposables.remove(it)
            countdownDisposableMap.remove(taskName)
        }


    /**
     * 延遲執行任務
     */
    fun delayTask(delayMs: Long, task: () -> Unit) {
        disposables.add(Observable.timer(delayMs, TimeUnit.MILLISECONDS, Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                task()
            })
    }
}