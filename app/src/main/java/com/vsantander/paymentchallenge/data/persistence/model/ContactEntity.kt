package com.vsantander.paymentchallenge.data.persistence.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "contactselected")
data class ContactEntity(
        @PrimaryKey
        val name: String,
        @ColumnInfo(name = "phone")
        val phone: String?,
        @ColumnInfo(name = "avatar")
        val avatar: String
)