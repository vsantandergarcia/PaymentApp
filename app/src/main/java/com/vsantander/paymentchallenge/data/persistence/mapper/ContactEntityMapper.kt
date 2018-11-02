package com.vsantander.paymentchallenge.data.persistence.mapper

import com.vsantander.paymentchallenge.data.persistence.model.ContactEntity
import com.vsantander.paymentchallenge.domain.model.Contact
import javax.inject.Inject

class ContactEntityMapper @Inject constructor() {

    fun toEntity(value: ContactEntity): Contact = Contact(
            name = value.name,
            phone = value.phone ?: "",
            avatar = value.avatar,
            isSelected = false
    )

    fun toEntity(values: List<ContactEntity>): List<Contact> = values.map { toEntity(it) }

    fun fromEntity(value: Contact): ContactEntity = ContactEntity(
            name = value.name,
            phone = value.phone ?: "",
            avatar = value.avatar
    )

    fun fromEntity(values: List<Contact>): List<ContactEntity> = values.map { fromEntity(it) }
}