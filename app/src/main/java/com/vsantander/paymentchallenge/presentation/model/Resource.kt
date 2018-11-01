package com.vsantander.paymentchallenge.presentation.model

data class Resource<ResultType>(val status: Status, var data: ResultType? = null, val msg: String? = null ) {

    companion object {
        /**
         * Creates [Resource] object with `SUCCESS` status and [data].
         */
        fun <ResultType> success(data: ResultType): Resource<ResultType> = Resource(Status.SUCCESS, data)

        /**
         * Creates [Resource] object with `LOADING` status to notify
         * the UI to showing loading.
         */
        fun <ResultType> loading(): Resource<ResultType> = Resource(Status.LOADING)

        /**
         * Creates [Resource] object with `ERROR` status and [message].
         */
        fun <ResultType> error(throwable: Throwable): Resource<ResultType> =
                Resource(status = Status.FAILED, msg = throwable.message)
    }
}