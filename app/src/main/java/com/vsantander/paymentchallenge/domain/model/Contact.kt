package com.vsantander.paymentchallenge.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Contact(
        val name: String,
        val phone: String?,
        val avatar: String
) : Parcelable