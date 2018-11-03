package com.vsantander.paymentchallenge.utils.factory

import com.vsantander.paymentchallenge.data.remote.model.ContactTO

/**
 * Factory class for ContactTO related instances
 */
class ContactTOFactory {

    companion object {

        fun makeContactTOList(count: Int): List<ContactTO> {
            val contactTOInfoList = mutableListOf<ContactTO>()
            repeat(count) {
                contactTOInfoList.add(makeContactTOModel())
            }
            return contactTOInfoList
        }

        private fun makeContactTOModel(): ContactTO {
            return ContactTO(
                    name = DataFactory.randomUuid(),
                    thumbnail = ThumbnailTOFactory.makeThumbnailTOModel()
            )
        }
    }

}