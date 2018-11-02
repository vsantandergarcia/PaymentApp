package com.vsantander.paymentchallenge.domain.usecase

import com.vsantander.paymentchallenge.data.repository.ContactRepositoryImpl
import com.vsantander.paymentchallenge.domain.model.Contact
import com.vsantander.paymentchallenge.domain.usecase.base.SingleUseCase
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class GetContacts @Inject constructor(
        private val repository: ContactRepositoryImpl
) : SingleUseCase<Boolean, List<Contact>>() {

    override fun buildUseCase(params: Boolean?): Single<List<Contact>> {
        if (params!!) {
            return repository.getCharacters()
                    .zipWith(repository.getPhoneContacts(),
                            BiFunction<List<Contact>, List<Contact>, List<Contact>>
                            { caracters, phoneContacts ->
                                caracters + phoneContacts
                            })
                    .map { contacts ->
                        contacts.sortedBy { it.name }
                    }
        } else {
            return repository.getCharacters()
        }
    }

}