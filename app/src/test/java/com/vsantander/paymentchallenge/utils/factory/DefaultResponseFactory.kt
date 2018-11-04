package com.vsantander.paymentchallenge.utils.factory

import com.vsantander.paymentchallenge.data.remote.model.ContactTO
import com.vsantander.paymentchallenge.data.remote.model.DefaultResponse

class DefaultResponseFactory() {

    companion object {

        fun makeDefaultResponse(contactTOList: List<ContactTO>): DefaultResponse<List<ContactTO>> {
            return DefaultResponse(
                    code = DataFactory.randomInt(),
                    status = DataFactory.randomUuid(),
                    copyright = DataFactory.randomUuid(),
                    attributionText = DataFactory.randomUuid(),
                    attributionHTML = DataFactory.randomUuid(),
                    data = DataInfoFactory.makeDataInfoModel(contactTOList)
            )
        }
    }
}