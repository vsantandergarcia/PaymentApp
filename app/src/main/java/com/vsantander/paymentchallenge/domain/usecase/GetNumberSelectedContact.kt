package com.vsantander.paymentchallenge.domain.usecase

import com.vsantander.paymentchallenge.data.repository.ContactRepositoryImpl
import com.vsantander.paymentchallenge.domain.usecase.base.FlowableResponseUseCase
import io.reactivex.Flowable
import javax.inject.Inject

class GetNumberSelectedContact @Inject constructor(
        private val repository: ContactRepositoryImpl
) : FlowableResponseUseCase<Int>() {

    override fun buildUseCase(params: NoRequestValues?): Flowable<Int> {
        return repository.getSelectedContactsCount()
    }
}