package com.vsantander.paymentchallenge.di

import com.vsantander.paymentchallenge.presentation.PaymentApp
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
        modules = arrayOf(
                AndroidSupportInjectionModule::class,
                AppModule::class,
                ActivityModule::class)
)
abstract class AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: PaymentApp): Builder

        fun build(): AppComponent
    }

    abstract fun inject(application: PaymentApp)
}