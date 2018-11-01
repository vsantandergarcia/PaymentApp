package com.vsantander.paymentchallenge.data.repository

import com.vsantander.paymentchallenge.domain.model.Contact
import io.reactivex.Single

interface ContactRepository {

    fun getCharacters(): Single<List<Contact>>

}