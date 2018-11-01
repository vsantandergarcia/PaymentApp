package com.vsantander.paymentchallenge.domain.usecase.base

import io.reactivex.Single

abstract class SingleResponseUseCase<T>
    : RxUseCase<RxUseCase.NoRequestValues, Single<T>>()