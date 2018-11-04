package com.vsantander.paymentchallenge.presentation

import android.app.Activity
import android.app.Application
import android.util.Log
import com.facebook.stetho.Stetho
import com.vsantander.paymentchallenge.BuildConfig
import com.vsantander.paymentchallenge.di.AppInjector
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import timber.log.Timber
import javax.inject.Inject

class PaymentApp : Application(), HasActivityInjector {

    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>

    override fun activityInjector(): AndroidInjector<Activity> = activityInjector

    override fun onCreate() {
        super.onCreate()

        // Dagger 2 injection
        AppInjector.init(this)

        //init Stetho
        Stetho.initializeWithDefaults(this)

        //init Timber
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(ReleaseLogTree())
        }
    }

    internal inner class ReleaseLogTree : Timber.Tree() {

        private val MAX_LOG_LENGTH = 4000

        override fun isLoggable(tag: String?, priority: Int): Boolean =
                !(priority == Log.VERBOSE || priority == Log.DEBUG || priority == Log.INFO)

        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            if (isLoggable(tag, priority)) {
                if (message.length < MAX_LOG_LENGTH) {
                    if (priority == Log.ASSERT) {
                        Log.wtf(tag, message)
                    } else {
                        Log.println(priority, tag, message)
                    }
                    return
                }

                val length = message.length
                var i = 0
                while (i < length) {
                    var newLine = message.indexOf('\n', i)
                    newLine = if (newLine != -1) newLine else length
                    do {
                        val end = Math.min(newLine, i + MAX_LOG_LENGTH)
                        val part = message.substring(i, end)
                        if (priority == Log.ASSERT) {
                            Log.wtf(tag, part)
                        } else {
                            Log.println(priority, tag, part)
                        }
                        i = end
                    } while (i < newLine)
                    i++
                }
            }
        }
    }
}
