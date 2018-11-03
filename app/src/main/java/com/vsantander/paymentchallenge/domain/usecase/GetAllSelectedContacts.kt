package com.vsantander.paymentchallenge.domain.usecase

import com.vsantander.paymentchallenge.data.repository.ContactRepositoryImpl
import com.vsantander.paymentchallenge.domain.model.Contact
import com.vsantander.paymentchallenge.domain.usecase.base.SingleResponseUseCase
import io.reactivex.Single
import javax.inject.Inject

class GetAllSelectedContacts @Inject constructor(
        private val repository: ContactRepositoryImpl
) : SingleResponseUseCase<List<Contact>>() {

    override fun buildUseCase(params: NoRequestValues?): Single<List<Contact>> {
        return repository.getAllSelectedContacts()
    }

}