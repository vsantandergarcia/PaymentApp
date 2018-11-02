package com.vsantander.paymentchallenge.data.persistence

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.vsantander.paymentchallenge.data.persistence.dao.SelectedContactsDao
import com.vsantander.paymentchallenge.data.persistence.model.ContactEntity

@Database(entities = [(ContactEntity::class)], version = 1, exportSchema = false)
abstract class Database : RoomDatabase()  {

    abstract fun selectedContactsDao(): SelectedContactsDao

}