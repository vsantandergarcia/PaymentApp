package com.vsantander.paymentchallenge.domain.usecase.base

import io.reactivex.Completable

abstract class CompletableResponseUseCase
    : RxUseCase<RxUseCase.NoRequestValues, Completable> ()