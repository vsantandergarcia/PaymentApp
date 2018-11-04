package com.vsantander.paymentchallenge.repository

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.content.ContentResolver
import android.database.Cursor
import android.provider.ContactsContract
import com.vsantander.paymentchallenge.data.persistence.Database
import com.vsantander.paymentchallenge.data.persistence.dao.SelectedContactsDao
import com.vsantander.paymentchallenge.data.persistence.mapper.ContactEntityMapper
import com.vsantander.paymentchallenge.data.remote.RestClient
import com.vsantander.paymentchallenge.data.remote.mapper.ContactTOMapper
import com.vsantander.paymentchallenge.data.repository.ContactRepositoryImpl
import com.vsantander.paymentchallenge.utils.RxImmediateSchedulerRule
import com.vsantander.paymentchallenge.utils.factory.ContactEntityFactory
import com.vsantander.paymentchallenge.utils.factory.ContactFactory
import com.vsantander.paymentchallenge.utils.factory.ContactTOFactory
import com.vsantander.paymentchallenge.utils.factory.DefaultResponseFactory
import io.reactivex.Flowable
import io.reactivex.Single
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ContactRepositoryTest {

    companion object {
        private const val NUMBER_ITEMS = 5
    }

    @Suppress("unused")
    @get:Rule // used to make all live data calls sync
    val instantExecutor = InstantTaskExecutorRule()

    @Rule
    @JvmField
    val testSchedulerRule = RxImmediateSchedulerRule()

    @Mock
    lateinit var service: RestClient

    @Mock
    lateinit var database: Database

    @Mock
    lateinit var selectedContactsDao: SelectedContactsDao

    @Mock
    lateinit var contentResolver: ContentResolver

    @Mock
    lateinit var cursor: Cursor

    lateinit var contactTOMapper: ContactTOMapper

    lateinit var contactEntityMapper: ContactEntityMapper

    lateinit var repository: ContactRepositoryImpl

    @Before
    fun setUp() {
        contactTOMapper = ContactTOMapper()
        contactEntityMapper = ContactEntityMapper()
        repository = ContactRepositoryImpl(service, contactTOMapper, contentResolver,
                database, contactEntityMapper)
    }

    @Test
    fun getCharactersOk() {
        val contactTOList = ContactTOFactory.makeContactTOList(NUMBER_ITEMS)
        val contactEntityList = ContactEntityFactory.makeContactEntityList(NUMBER_ITEMS)
        val contactList = contactTOList.map { contactTOMapper.toEntity(it) }
        Mockito.`when`(service.getCharacters(Mockito.anyLong(), Mockito.anyString(),
                Mockito.anyString(), Mockito.anyInt(), Mockito.anyString()))
                .thenReturn(Single.just(DefaultResponseFactory.makeDefaultResponse(contactTOList)))
        Mockito.`when`(database.selectedContactsDao()).thenReturn(selectedContactsDao)
        Mockito.`when`(database.selectedContactsDao().getAll())
                .thenReturn(Single.just(contactEntityList))

        val testObserver = repository.getCharacters().test()
        testObserver.awaitTerminalEvent()

        testObserver.assertComplete()
        testObserver.assertNoErrors()

        val result = testObserver.values()[0]
        MatcherAssert.assertThat(result.size, CoreMatchers.`is`(NUMBER_ITEMS))
        assert(result == contactList)
    }

    @Test
    fun getCharactersError() {
        Mockito.`when`(service.getCharacters(Mockito.anyLong(), Mockito.anyString(),
                Mockito.anyString(), Mockito.anyInt(), Mockito.anyString()))
                .thenReturn(Single.error(RuntimeException()))
        Mockito.`when`(database.selectedContactsDao()).thenReturn(selectedContactsDao)
        Mockito.`when`(database.selectedContactsDao().getAll())
                .thenReturn(Single.error(RuntimeException()))

        val testObserver = repository.getCharacters().test()
        testObserver.awaitTerminalEvent()

        testObserver.assertNotComplete()
        testObserver.assertError(RuntimeException::class.java)
    }

    @Test
    fun getPhoneContactsOk() {
        val contactEntityList = ContactEntityFactory.makeContactEntityList(NUMBER_ITEMS)
        Mockito.`when`(database.selectedContactsDao()).thenReturn(selectedContactsDao)
        Mockito.`when`(database.selectedContactsDao().getAll())
                .thenReturn(Single.just(contactEntityList))

        val testObserver = repository.getPhoneContacts().test()
        testObserver.awaitTerminalEvent()

        testObserver.assertComplete()
        testObserver.assertNoErrors()

        val result = testObserver.values()[0]
        MatcherAssert.assertThat(result.size, CoreMatchers.`is`(0))
    }

    @Test
    fun getPhoneContactsError() {
        Mockito.`when`(database.selectedContactsDao()).thenReturn(selectedContactsDao)
        Mockito.`when`(database.selectedContactsDao().getAll())
                .thenReturn(Single.error(RuntimeException()))

        val testObserver = repository.getPhoneContacts().test()
        testObserver.awaitTerminalEvent()

        testObserver.assertNotComplete()
        testObserver.assertError(RuntimeException::class.java)
    }

    @Test
    fun getAllSelectedContactsOk() {
        val contactEntityList = ContactEntityFactory.makeContactEntityList(NUMBER_ITEMS)
        val contactList = contactEntityList.map { contactEntityMapper.toEntity(it) }
        Mockito.`when`(database.selectedContactsDao()).thenReturn(selectedContactsDao)
        Mockito.`when`(database.selectedContactsDao().getAll())
                .thenReturn(Single.just(contactEntityList))

        val testObserver = repository.getAllSelectedContacts().test()
        testObserver.awaitTerminalEvent()

        testObserver.assertComplete()
        testObserver.assertNoErrors()

        val result = testObserver.values()[0]
        MatcherAssert.assertThat(result.size, CoreMatchers.`is`(NUMBER_ITEMS))
        assert(result == contactList)
    }

    @Test
    fun getAllSelectedContactsError() {
        Mockito.`when`(database.selectedContactsDao()).thenReturn(selectedContactsDao)
        Mockito.`when`(database.selectedContactsDao().getAll())
                .thenReturn(Single.error(RuntimeException()))

        val testObserver = repository.getAllSelectedContacts().test()
        testObserver.awaitTerminalEvent()

        testObserver.assertNotComplete()
        testObserver.assertError(RuntimeException::class.java)
    }

    @Test
    fun getSelectedContactsCountOk() {
        Mockito.`when`(database.selectedContactsDao()).thenReturn(selectedContactsDao)
        Mockito.`when`(database.selectedContactsDao().count())
                .thenReturn(Flowable.just(NUMBER_ITEMS))

        val testObserver = repository.getSelectedContactsCount().test()
        testObserver.awaitTerminalEvent()

        testObserver.assertComplete()
        testObserver.assertNoErrors()

        val result = testObserver.values()[0]
        MatcherAssert.assertThat(result, CoreMatchers.`is`(NUMBER_ITEMS))
    }

    @Test
    fun getSelectedContactsCountError() {
        Mockito.`when`(database.selectedContactsDao()).thenReturn(selectedContactsDao)
        Mockito.`when`(database.selectedContactsDao().count())
                .thenReturn(Flowable.error(RuntimeException()))

        val testObserver = repository.getSelectedContactsCount().test()
        testObserver.awaitTerminalEvent()

        testObserver.assertNotComplete()
        testObserver.assertError(RuntimeException::class.java)
    }

    @Test
    fun saveSelectedContactOk() {
        val item = ContactFactory.makeContactModel()
        Mockito.`when`(database.selectedContactsDao()).thenReturn(selectedContactsDao)

        val testObserver = repository.saveSelectedContact(item).test()
        testObserver.awaitTerminalEvent()

        testObserver.assertComplete()
        testObserver.assertNoErrors()
    }

    @Test
    fun saveSelectedContactError() {
        val item = ContactFactory.makeContactModel()
        val testObserver = repository.saveSelectedContact(item).test()
        testObserver.awaitTerminalEvent()

        testObserver.assertNotComplete()
        testObserver.assertError(RuntimeException::class.java)
    }

    @Test
    fun deleteSelectedContactOk() {
        val item = ContactFactory.makeContactModel()
        Mockito.`when`(database.selectedContactsDao()).thenReturn(selectedContactsDao)

        val testObserver = repository.deleteSelectedContact(item).test()
        testObserver.awaitTerminalEvent()

        testObserver.assertComplete()
        testObserver.assertNoErrors()
    }

    @Test
    fun deleteSelectedContactError() {
        val item = ContactFactory.makeContactModel()
        val testObserver = repository.deleteSelectedContact(item).test()
        testObserver.awaitTerminalEvent()

        testObserver.assertNotComplete()
        testObserver.assertError(RuntimeException::class.java)
    }

    @Test
    fun deleteAllSelectedContactsOk() {
        Mockito.`when`(database.selectedContactsDao()).thenReturn(selectedContactsDao)

        val testObserver = repository.deleteAllSelectedContacts().test()
        testObserver.awaitTerminalEvent()

        testObserver.assertComplete()
        testObserver.assertNoErrors()
    }

    @Test
    fun deleteAllSelectedContactsError() {
        val testObserver = repository.deleteAllSelectedContacts().test()
        testObserver.awaitTerminalEvent()

        testObserver.assertNotComplete()
        testObserver.assertError(RuntimeException::class.java)
    }

}