package com.vsantander.paymentchallenge.presentation.contactlist

import android.arch.lifecycle.MutableLiveData
import com.vsantander.paymentchallenge.data.repository.ContactRepositoryImpl
import com.vsantander.paymentchallenge.domain.model.Contact
import com.vsantander.paymentchallenge.domain.usecase.GetCharacters
import com.vsantander.paymentchallenge.presentation.base.viewmodel.BaseViewModel
import com.vsantander.paymentchallenge.presentation.model.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class ContactListViewModel @Inject constructor(
        private val getCharacters: GetCharacters
): BaseViewModel() {

    val resource = MutableLiveData<Resource<List<Contact>>>()

    fun loadInfo() {
        resource.value = Resource.loading()

        disposables += getCharacters.buildUseCase()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            Timber.d("loadInfo.onComplete")
                            resource.value = Resource.success(it)
                        },
                        onError = {
                            Timber.e("loadInfo.onError ${it.message}")
                            resource.value = Resource.error(it)
                        }
                )
    }
}