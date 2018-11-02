package com.vsantander.paymentchallenge.data.repository

import com.vsantander.paymentchallenge.domain.model.Contact
import io.reactivex.Single

interface ContactRepository {

    /**
     * Gets a List of characters from marvel
     *
     * @return List<Contact> The list of contacts from marvel
     */
    fun getCharacters(): Single<List<Contact>>

    /**
     * Gets a List of contacts from the device
     *
     * @return List<Contact> The list of contacts from device
     */
    fun getPhoneContacts(): Single<List<Contact>>

}