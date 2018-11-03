package com.vsantander.paymentchallenge.database.database

import android.arch.persistence.room.EmptyResultSetException
import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.vsantander.paymentchallenge.data.persistence.Database
import com.vsantander.paymentchallenge.data.persistence.model.ContactEntity
import com.vsantander.paymentchallenge.database.utils.RxImmediateSchedulerRuleInstrumentation
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class DatabaseTest {

    companion object {
        private const val NUMBER_ITEMS = 5

        private val items = (1..NUMBER_ITEMS).map {
            ContactEntity(
                    name = UUID.randomUUID().toString(),
                    phone = UUID.randomUUID().toString(),
                    avatar = UUID.randomUUID().toString()
            )
        }
    }

    private lateinit var db: Database

    @Rule
    @JvmField
    val testSchedulerRule = RxImmediateSchedulerRuleInstrumentation()

    @Before
    fun initDb() {
        db = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                Database::class.java).build()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(InterruptedException::class)
    fun getAllReturnOk() {
        items.forEach { db.selectedContactsDao().insertOrUpdate(it) }

        val testObserver = db.selectedContactsDao().getAll().test()
        testObserver.awaitTerminalEvent()

        testObserver.assertComplete()
        testObserver.assertNoErrors()

        val result = testObserver.values()[0]
        MatcherAssert.assertThat(result.size, CoreMatchers.`is`(NUMBER_ITEMS))
        assert(result == items)
    }

    @Test
    @Throws(InterruptedException::class)
    fun getReturnOk() {
        val item = items[0]
        db.selectedContactsDao().insertOrUpdate(item)

        val testObserver = db.selectedContactsDao().get(item.name).test()
        testObserver.awaitTerminalEvent()

        testObserver.assertComplete()
        testObserver.assertNoErrors()

        val result = testObserver.values()[0]
        assert(result == item)
    }

    @Test
    @Throws(InterruptedException::class)
    fun insertOrUpdateOk() {
        items.forEach { db.selectedContactsDao().insertOrUpdate(it) }

        val testObserver = db.selectedContactsDao().getAll().test()
        testObserver.awaitTerminalEvent()

        testObserver.assertComplete()
        testObserver.assertNoErrors()

        val result = testObserver.values()[0]
        MatcherAssert.assertThat(result.size, CoreMatchers.`is`(NUMBER_ITEMS))
        assert(result == items)
    }

    @Test
    @Throws(InterruptedException::class)
    fun countOk() {
        items.forEach { db.selectedContactsDao().insertOrUpdate(it) }

        val testObserver = db.selectedContactsDao().count().test()
        testObserver.awaitCount(NUMBER_ITEMS)

        testObserver.assertNoErrors()
        val result = testObserver.values()[0]
        MatcherAssert.assertThat(result, CoreMatchers.`is`(NUMBER_ITEMS))
    }

    @Test
    @Throws(InterruptedException::class)
    fun deleteOk() {
        val item = items[0]
        db.selectedContactsDao().insertOrUpdate(item)

        db.selectedContactsDao().delete(item.name)

        val testObserver = db.selectedContactsDao().get(item.name).test()
        testObserver.awaitTerminalEvent()
        testObserver.assertError(EmptyResultSetException::class.java)
    }

    @Test
    @Throws(InterruptedException::class)
    fun deleteAllOk() {
        items.forEach { db.selectedContactsDao().insertOrUpdate(it) }

        db.selectedContactsDao().deleteAll()

        val testObserver = db.selectedContactsDao().getAll().test()
        testObserver.assertComplete()
        testObserver.assertNoErrors()

        val result = testObserver.values()[0]
        assert(result.isEmpty())
    }
}