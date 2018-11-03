package com.vsantander.paymentchallenge.domain.usecase

import com.vsantander.paymentchallenge.domain.usecase.base.CompletableUseCase
import com.vsantander.paymentchallenge.domain.usecase.base.RxUseCase
import io.reactivex.Completable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class PerformFakePayment @Inject constructor() : CompletableUseCase<RxUseCase.NoRequestValues>() {

    override fun buildUseCase(params: NoRequestValues?): Completable {
        // Fake behavior TODO finish real payment
        return Completable.complete().delay(2, TimeUnit.SECONDS)
    }
}