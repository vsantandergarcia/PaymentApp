package com.vsantander.paymentchallenge.utils.factory

import com.vsantander.paymentchallenge.data.persistence.model.ContactEntity

class ContactEntityFactory {

    companion object {

        fun makeContactEntityList(count: Int): List<ContactEntity> {
            val contactEntityList = mutableListOf<ContactEntity>()
            repeat(count) {
                contactEntityList.add(makeContactEntityModel())
            }
            return contactEntityList
        }

        fun makeContactEntityModel(): ContactEntity {
            return ContactEntity(
                    name = DataFactory.randomUuid(),
                    phone = DataFactory.randomUuid(),
                    avatar = DataFactory.randomUuid()
            )
        }
    }
}