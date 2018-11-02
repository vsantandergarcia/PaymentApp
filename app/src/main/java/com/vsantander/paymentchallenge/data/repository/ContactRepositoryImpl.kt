package com.vsantander.paymentchallenge.data.repository

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.provider.ContactsContract
import com.vsantander.paymentchallenge.data.persistence.Database
import com.vsantander.paymentchallenge.data.persistence.mapper.ContactEntityMapper
import com.vsantander.paymentchallenge.data.remote.RestClient
import com.vsantander.paymentchallenge.data.remote.mapper.ContactTOMapper
import com.vsantander.paymentchallenge.data.remote.utils.APIParamsProvider
import com.vsantander.paymentchallenge.domain.model.Contact
import com.vsantander.paymentchallenge.utils.Constants
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.rxkotlin.zipWith
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactRepositoryImpl @Inject constructor(
        private val restClient: RestClient,
        private val contactTOMapper: ContactTOMapper,
        private val contentResolver: ContentResolver,
        private val database: Database,
        private val contactEntityMapper: ContactEntityMapper
) : ContactRepository {

    override fun getCharacters(): Single<List<Contact>> {
        val timestamp = APIParamsProvider.timestamp()
        return restClient.getCharacters(
                timestamp = timestamp,
                apikey = Constants.API_KEY,
                hash = APIParamsProvider.hash(timestamp),
                limit = Constants.LIMIT_CHARACTERS,
                orderBy = Constants.CHARACTERS_ORDERED_BY
        ).map {
            contactTOMapper.toEntity(it.data.results)
        }.zipWith(getAllSelectedContacts().map { contact -> contact.map { it.name } },
                BiFunction<List<Contact>, List<String>, List<Contact>>
                { contacts, selectedContacts ->
                    contacts.onEach { it.isSelected = selectedContacts.contains(it.name) }
                })
    }

    @SuppressLint("Recycle")
    override fun getPhoneContacts(): Single<List<Contact>> {
        val contactList = arrayListOf<Contact>()
        val cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null)
        cursor?.let { cursor ->

            if (cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val id = cursor.getString(
                            cursor.getColumnIndex(ContactsContract.Contacts._ID))
                    val name = cursor.getString(
                            cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                    if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                        val pCur = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                arrayOf(id), null)
                        while (pCur!!.moveToNext()) {
                            val number = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                            contactList.add(
                                    Contact(name = name,
                                            phone = number,
                                            avatar = "",
                                            isSelected = false))
                        }
                        pCur.close()
                    }
                }
            }
            cursor.close()
        }

        return Single.just(contactList)
                .zipWith(getAllSelectedContacts().map { contact -> contact.map { it.name } },
                        BiFunction<List<Contact>, List<String>, List<Contact>>
                        { contacts, selectedContacts ->
                            contacts.onEach { it.isSelected = selectedContacts.contains(it.name) }
                        })
    }

    override fun getAllSelectedContacts(): Single<List<Contact>> {
        return database.selectedContactsDao().getAll()
                .map { contactEntityMapper.toEntity(it) }
    }

    override fun saveSelectedContact(contact: Contact): Completable {
        return Completable.fromCallable {
            database.selectedContactsDao().insertOrUpdate(
                    contactEntityMapper.fromEntity(contact)
            )
        }
    }

    override fun deleteSelectedContact(contact: Contact): Completable {
        return Completable.fromCallable {
            database.selectedContactsDao().delete(contact.name)
        }
    }


}