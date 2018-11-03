package com.vsantander.paymentchallenge.domain.usecase

import com.vsantander.paymentchallenge.data.repository.ContactRepositoryImpl
import com.vsantander.paymentchallenge.domain.usecase.base.CompletableResponseUseCase
import io.reactivex.Completable
import javax.inject.Inject

class DeleteAllSelectedContacts @Inject constructor(
        private val repository: ContactRepositoryImpl
) : CompletableResponseUseCase() {

    override fun buildUseCase(params: NoRequestValues?): Completable {
        return repository.deleteAllSelectedContacts()
    }

}