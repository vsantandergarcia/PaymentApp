package com.vsantander.paymentchallenge.data.remote.utils

import com.vsantander.paymentchallenge.BuildConfig
import com.vsantander.paymentchallenge.utils.Constants
import com.vsantander.paymentchallenge.utils.extension.md5


object APIParamsProvider {

    fun timestamp(): Long = System.currentTimeMillis()

    fun hash(timestamp: Long): String = (timestamp.toString() + BuildConfig.PRIVATE_KEY + Constants.API_KEY).md5()

}