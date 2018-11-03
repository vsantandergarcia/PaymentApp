package com.vsantander.paymentchallenge.presentation.summary

import android.arch.lifecycle.MutableLiveData
import com.vsantander.paymentchallenge.domain.model.Contact
import com.vsantander.paymentchallenge.domain.usecase.DeleteAllSelectedContacts
import com.vsantander.paymentchallenge.domain.usecase.GetAllSelectedContacts
import com.vsantander.paymentchallenge.domain.usecase.PerformFakePayment
import com.vsantander.paymentchallenge.presentation.base.viewmodel.BaseViewModel
import com.vsantander.paymentchallenge.presentation.model.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class SummaryViewModel @Inject constructor(
        private val getAllSelectedContacts: GetAllSelectedContacts,
        private val performFakePayment: PerformFakePayment,
        private val deleteAllSelectedContacts: DeleteAllSelectedContacts
): BaseViewModel() {

    val resource = MutableLiveData<Resource<List<Contact>>>()

    val paymentFinished = MutableLiveData<Boolean>()

    init {
        loadSelectedContacts()
    }

    fun loadSelectedContacts() {
        resource.value = Resource.loading()

        disposables += getAllSelectedContacts.buildUseCase()
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

    fun performFakePayment() {
        disposables += performFakePayment.buildUseCase()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onComplete = {
                            Timber.d("performFakePayment.onComplete")
                            deleteAllSelectedContacts()
                        },
                        onError = {
                            Timber.e("performFakePayment.onError ${it.message}")
                        }
                )
    }

    private fun deleteAllSelectedContacts() {

        disposables += deleteAllSelectedContacts.buildUseCase()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onComplete = {
                            Timber.d("deleteAllSelectedContacts.onComplete")
                            paymentFinished.value = true

                        },
                        onError = {
                            Timber.e("deleteAllSelectedContacts.onError ${it.message}")
                            paymentFinished.value = false
                        }
                )
    }

}