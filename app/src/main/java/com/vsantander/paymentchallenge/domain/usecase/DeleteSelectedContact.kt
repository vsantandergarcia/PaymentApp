package com.vsantander.paymentchallenge.domain.usecase

import com.vsantander.paymentchallenge.data.repository.ContactRepositoryImpl
import com.vsantander.paymentchallenge.domain.model.Contact
import com.vsantander.paymentchallenge.domain.usecase.base.CompletableUseCase
import io.reactivex.Completable
import javax.inject.Inject

class DeleteSelectedContact @Inject constructor(
        private val repository: ContactRepositoryImpl
) : CompletableUseCase<Contact>() {

    override fun buildUseCase(params: Contact?): Completable {
        return repository.deleteSelectedContact(params!!)
    }

}