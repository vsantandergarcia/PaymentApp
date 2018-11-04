package com.vsantander.paymentchallenge.data.remote.model

/**
 * Server exceptions
 */

open class ServerException constructor(message: String) : RuntimeException(message)

open class InvalidParamException constructor(message: String) : RuntimeException(message)