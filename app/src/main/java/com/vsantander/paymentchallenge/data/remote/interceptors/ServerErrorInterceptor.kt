package com.vsantander.paymentchallenge.data.remote.interceptors

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.vsantander.paymentchallenge.data.remote.model.DefaultResponse
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody

class ServerErrorInterceptor : Interceptor {
    private val gson: Gson = Gson()
    override fun intercept(chain: Interceptor.Chain?): Response {
        val request = chain!!.request()
        val response = chain.proceed(request)

        val contentType = response.body()!!.contentType()
        val bodyString = response.body()!!.string()

        if (!response.isSuccessful) {
            //http errors
            ServerErrorHandler.throwHttpErrorIfExists(response)

            val type = object : TypeToken<DefaultResponse<Any>>() {}.type
            val modelResponse: DefaultResponse<Any> =
                    gson.fromJson<DefaultResponse<Any>>(bodyString, type)

            //server concrete errors
            val exception = ServerErrorHandler.getServerError(modelResponse)

            exception?.let {
                throw  it
            }
        }

        // Re-create the response before returning it because body can be read only once
        return response.newBuilder()
                .protocol(response.protocol())
                .message(response.message())
                .message(response.message())
                .code(response.code())
                .body(ResponseBody.create(contentType, bodyString))
                .build()
    }
}