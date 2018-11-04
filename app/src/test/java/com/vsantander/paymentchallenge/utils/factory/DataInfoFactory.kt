package com.vsantander.paymentchallenge.utils.factory

import com.vsantander.paymentchallenge.data.remote.model.ContactTO
import com.vsantander.paymentchallenge.data.remote.model.DataInfo

class DataInfoFactory {

    companion object {

        fun makeDataInfoModel(contactTOList: List<ContactTO>): DataInfo<List<ContactTO>> {
            return DataInfo(
                    offset = DataFactory.randomInt(),
                    limit = DataFactory.randomInt(),
                    total = DataFactory.randomInt(),
                    count = DataFactory.randomInt(),
                    results = contactTOList
            )
        }

    }

}