package com.vsantander.paymentchallenge.data.remote.model

import com.google.gson.annotations.SerializedName

data class ThumbnailTO(
        @SerializedName("path")
        val path: String,

        @SerializedName("extension")
        val extension: String
)