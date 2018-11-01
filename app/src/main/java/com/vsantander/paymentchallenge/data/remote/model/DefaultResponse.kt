package com.vsantander.paymentchallenge.data.remote.model

import com.google.gson.annotations.SerializedName

data class DefaultResponse<T>(
        @SerializedName("code")
        val code: Int,

        @SerializedName("status")
        val status: String,

        @SerializedName("copyright")
        val copyright: String,

        @SerializedName("attributionText")
        val attributionText: String,

        @SerializedName("attributionHTML")
        val attributionHTML: String,

        @SerializedName("data")
        val data: DataInfo<T>
)
