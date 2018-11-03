package com.vsantander.paymentchallenge.domain.usecase.base

import io.reactivex.Flowable

abstract class FlowableResponseUseCase<T>
: RxUseCase<RxUseCase.NoRequestValues, Flowable<T>>() {
}