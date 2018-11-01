package com.vsantander.paymentchallenge.data.remote.model

import com.google.gson.annotations.SerializedName

data class ContactTO (
        @SerializedName("name")
        val name: String,

        @SerializedName("thumbnail")
        val thumbnail: ThumbnailTO
)