package com.vsantander.paymentchallenge.di

import com.vsantander.paymentchallenge.presentation.contactlist.ContactListActivity
import com.vsantander.paymentchallenge.presentation.splash.SplashActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module(includes = [(ViewModelModule::class)])
abstract class ActivityModule {

    @ContributesAndroidInjector
    internal abstract fun contributeSplashActivity(): SplashActivity

    @ContributesAndroidInjector
    internal abstract fun contributeContactListActivity(): ContactListActivity

}