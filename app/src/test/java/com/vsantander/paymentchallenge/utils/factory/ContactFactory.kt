package com.vsantander.paymentchallenge.utils.factory

import com.vsantander.paymentchallenge.domain.model.Contact


/**
 * Factory class for Contact related instances
 */
class ContactFactory {

    companion object {

        fun makeContactList(count: Int): List<Contact> {
            val contactInfoList = mutableListOf<Contact>()
            repeat(count) {
                contactInfoList.add(makeContactModel())
            }
            return contactInfoList
        }

        fun makeContactModel(): Contact {
            return Contact(
                    name = DataFactory.randomUuid(),
                    phone = DataFactory.randomUuid(),
                    avatar = DataFactory.randomUuid(),
                    isSelected = DataFactory.randomBoolean()
            )
        }
    }

}