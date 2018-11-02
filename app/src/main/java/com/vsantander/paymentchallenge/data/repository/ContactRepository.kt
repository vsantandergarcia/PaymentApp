package com.vsantander.paymentchallenge.data.repository

import com.vsantander.paymentchallenge.domain.model.Contact
import io.reactivex.Completable
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

    /**
     * Gets all the contacts mark as selected
     *
     * @return List<Contact> The list of contacts mark as selected
     */
    fun getAllSelectedContacts(): Single<List<Contact>>

    /**
     * Save a contact as a selected
     *
     * @return if the save is complete.
     */
    fun saveSelectedContact(contact: Contact) : Completable

    /**
     * Delete a contact as a selected
     *
     * @return if the delete is complete.
     */
    fun deleteSelectedContact(contact: Contact) : Completable

}