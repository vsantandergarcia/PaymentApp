package com.vsantander.paymentchallenge.presentation

import android.app.Activity
import android.app.Application
import com.facebook.stetho.Stetho
import com.vsantander.paymentchallenge.di.AppInjector
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
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
    }
}