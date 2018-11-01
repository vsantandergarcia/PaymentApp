package com.vsantander.paymentchallenge.data.remote

import com.vsantander.paymentchallenge.data.remote.model.ContactTO
import com.vsantander.paymentchallenge.data.remote.model.DefaultResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface RestClient {

    @GET("/v1/public/characters")
    fun getCharacters(@Query("ts") timestamp: Long,
                      @Query("apikey") apikey: String,
                      @Query("hash") hash: String,
                      @Query("limit") limit: Int,
                      @Query("orderBy") orderBy: String): Single<DefaultResponse<List<ContactTO>>>

}