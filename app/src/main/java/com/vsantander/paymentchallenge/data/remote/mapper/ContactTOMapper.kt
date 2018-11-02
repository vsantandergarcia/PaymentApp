package com.vsantander.paymentchallenge.data.remote.mapper

import com.vsantander.paymentchallenge.data.remote.model.ContactTO
import com.vsantander.paymentchallenge.data.remote.utils.AvatarUrlProvider
import com.vsantander.paymentchallenge.domain.model.Contact
import javax.inject.Inject

class ContactTOMapper @Inject constructor() {

    private fun toEntity(value: ContactTO): Contact = Contact(
            name = value.name,
            phone = "",
            avatar = AvatarUrlProvider.getAvatarFromThumbnail(value.thumbnail),
            isSelected = false)

    fun toEntity(values: List<ContactTO>): List<Contact> = values.map { toEntity(it) }
}