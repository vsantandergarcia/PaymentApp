package com.vsantander.paymentchallenge.presentation.contactlist

import android.arch.lifecycle.MutableLiveData
import com.vsantander.paymentchallenge.domain.model.Contact
import com.vsantander.paymentchallenge.domain.usecase.DeleteSelectedContact
import com.vsantander.paymentchallenge.domain.usecase.GetContacts
import com.vsantander.paymentchallenge.domain.usecase.GetNumberSelectedContact
import com.vsantander.paymentchallenge.domain.usecase.SaveSelectedContact
import com.vsantander.paymentchallenge.presentation.base.viewmodel.BaseViewModel
import com.vsantander.paymentchallenge.presentation.model.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class ContactListViewModel @Inject constructor(
        private val getContacts: GetContacts,
        private val saveSelectedContact: SaveSelectedContact,
        private val deleteSelectedContact: DeleteSelectedContact,
        private val getNumberSelectedContact: GetNumberSelectedContact
) : BaseViewModel() {

    init {
        getNumberSelectedContact()
    }

    val resource = MutableLiveData<Resource<List<Contact>>>()

    val currentNumberSelectedContacts = MutableLiveData<Int>()

    var readContactsPermissionAccepted = false

    fun loadContacts() {
        resource.value = Resource.loading()

        disposables += getContacts.buildUseCase(readContactsPermissionAccepted)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            Timber.d("loadContacts.onComplete")
                            resource.value = Resource.success(it)
                        },
                        onError = {
                            Timber.e("loadContacts.onError ${it.message}")
                            resource.value = Resource.error(it)
                        }
                )
    }

    fun saveSelectedContact(contact: Contact) {
        disposables += saveSelectedContact.buildUseCase(contact)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onComplete = {
                            Timber.d("saveSelectedContact.onComplete")
                        },
                        onError = {
                            Timber.e("saveSelectedContact.onError ${it.message}")
                        }
                )
    }

    fun deleteSelectedContact(contact: Contact) {
        disposables += deleteSelectedContact.buildUseCase(contact)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onComplete = {
                            Timber.d("deleteSelectedContact.onComplete")
                        },
                        onError = {
                            Timber.e("deleteSelectedContact.onError ${it.message}")
                        }
                )
    }

    fun getNumberSelectedContact() {
        disposables += getNumberSelectedContact.buildUseCase()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            Timber.d("getNumberSelectedContact.onNext")
                            currentNumberSelectedContacts.value = it
                        },
                        onError = {
                            Timber.e("getNumberSelectedContact.onError ${it.message}")
                            currentNumberSelectedContacts.value = 0
                        }
                )
    }

}