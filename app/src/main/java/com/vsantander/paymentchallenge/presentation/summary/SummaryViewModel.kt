package com.vsantander.paymentchallenge.presentation.summary

import android.arch.lifecycle.MutableLiveData
import com.vsantander.paymentchallenge.domain.model.Contact
import com.vsantander.paymentchallenge.domain.usecase.GetSelectedContacts
import com.vsantander.paymentchallenge.presentation.base.viewmodel.BaseViewModel
import com.vsantander.paymentchallenge.presentation.model.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class SummaryViewModel @Inject constructor(
        private val getSelectedContacts: GetSelectedContacts
): BaseViewModel() {

    val resource = MutableLiveData<Resource<List<Contact>>>()

    init {
        loadSelectedContacts()
    }

    fun loadSelectedContacts() {
        resource.value = Resource.loading()

        disposables += getSelectedContacts.buildUseCase()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            Timber.d("loadSelectedContacts.onComplete")
                            resource.value = Resource.success(it)
                        },
                        onError = {
                            Timber.e("loadSelectedContacts.onError ${it.message}")
                            resource.value = Resource.error(it)
                        }
                )
    }

}