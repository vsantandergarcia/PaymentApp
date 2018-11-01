package com.vsantander.paymentchallenge.data.remote.model

import com.google.gson.annotations.SerializedName

data class DataInfo<T>(
        @SerializedName("offset")
        val offset: Int,

        @SerializedName("limit")
        val limit: Int,

        @SerializedName("total")
        val total: Int,

        @SerializedName("count")
        val count: Int,

        @SerializedName("results")
        val results: T
)
