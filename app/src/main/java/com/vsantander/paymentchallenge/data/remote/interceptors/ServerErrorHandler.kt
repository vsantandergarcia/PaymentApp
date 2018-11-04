package com.vsantander.paymentchallenge.data.remote.interceptors

import com.vsantander.paymentchallenge.data.remote.model.DefaultResponse
import com.vsantander.paymentchallenge.data.remote.model.InvalidParamException
import com.vsantander.paymentchallenge.data.remote.model.ServerException
import okhttp3.Response

object ServerErrorHandler {

    private const val NOT_FOUND = 404
    private const val BAD_REQUEST = 405
    private const val SUCCESS = 200

    /**
     * Http error
     */

    fun throwHttpErrorIfExists(response: Response) {
        when (response.code()) {
            NOT_FOUND,
            BAD_REQUEST -> {
                throw ServerException(response.message())
            }
        }
    }

    /**
     * Server error
     */

    fun getServerError(response: DefaultResponse<*>?): Exception? {
        if (response != null && response.code != SUCCESS) {
            return when (response.code) {
                409 -> InvalidParamException(response.status)
                else -> ServerException(response.status)
            }
        }
        return null
    }
}