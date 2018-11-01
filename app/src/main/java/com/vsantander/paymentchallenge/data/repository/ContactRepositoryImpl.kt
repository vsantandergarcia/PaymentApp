package com.vsantander.paymentchallenge.data.repository

import com.vsantander.paymentchallenge.data.remote.RestClient
import com.vsantander.paymentchallenge.data.remote.mapper.ContactTOMapper
import com.vsantander.paymentchallenge.data.remote.utils.APIParamsProvider
import com.vsantander.paymentchallenge.domain.model.Contact
import com.vsantander.paymentchallenge.utils.Constants
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactRepositoryImpl @Inject constructor(
        private val restClient: RestClient,
        private val mapper: ContactTOMapper
) : ContactRepository {

    override fun getCharacters(): Single<List<Contact>> {
        val timestamp = APIParamsProvider.timestamp()
        return restClient.getCharacters(
                timestamp = timestamp,
                apikey = Constants.API_KEY,
                hash = APIParamsProvider.hash(timestamp),
                limit = Constants.LIMIT_CHARACTERS,
                orderBy = Constants.CHARACTERS_ORDERED_BY
        ).map {
            mapper.toEntity(it.data.results)
        }
    }


}