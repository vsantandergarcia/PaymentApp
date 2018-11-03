package com.vsantander.paymentchallenge.data.persistence.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.vsantander.paymentchallenge.data.persistence.model.ContactEntity
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface SelectedContactsDao {

    @Query("SELECT * FROM contactselected")
    fun getAll(): Single<List<ContactEntity>>

    @Query("SELECT * FROM contactselected WHERE name = :name LIMIT 1")
    fun get(name: String): Single<ContactEntity>

    @Query("SELECT count(*) FROM contactselected")
    fun count(): Flowable<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdate(item: ContactEntity)

    @Query("DELETE FROM contactselected WHERE name =:name")
    fun delete(name: String)

}