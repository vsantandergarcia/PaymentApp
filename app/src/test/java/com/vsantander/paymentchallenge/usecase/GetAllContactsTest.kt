package com.vsantander.paymentchallenge.usecase

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.vsantander.paymentchallenge.data.repository.ContactRepositoryImpl
import com.vsantander.paymentchallenge.domain.usecase.GetAllContacts
import com.vsantander.paymentchallenge.utils.RxImmediateSchedulerRule
import com.vsantander.paymentchallenge.utils.factory.ContactFactory
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetAllContactsTest {

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
    lateinit var repository: ContactRepositoryImpl

    lateinit var getAllContacts: GetAllContacts

    @Before
    fun setUp() {
        getAllContacts = GetAllContacts(repository)
    }

    @Test
    fun getCharactersWithPhoneContactsOk() {
        val itemsList = ContactFactory.makeContactList(NUMBER_ITEMS)
        Mockito.`when`(repository.getCharacters())
                .thenReturn(Single.just(itemsList))
        Mockito.`when`(repository.getPhoneContacts())
                .thenReturn(Single.just(emptyList()))

        val testObserver = getAllContacts.buildUseCase(true).test()
        testObserver.awaitTerminalEvent()

        testObserver.assertComplete()
        testObserver.assertNoErrors()

        val result = testObserver.values()[0]
        assert(result.sortedBy { it.name } == itemsList.sortedBy { it.name })
    }

    @Test
    fun getCharactersOk() {
        val itemsList = ContactFactory.makeContactList(NUMBER_ITEMS)
        Mockito.`when`(repository.getCharacters())
                .thenReturn(Single.just(itemsList))

        val testObserver = getAllContacts.buildUseCase(false).test()
        testObserver.awaitTerminalEvent()

        testObserver.assertComplete()
        testObserver.assertNoErrors()

        val result = testObserver.values()[0]
        assert(result == itemsList)
    }

    @Test
    fun getCharactersWithPhoneContactsError() {
        Mockito.`when`(repository.getCharacters())
                .thenReturn(Single.error(RuntimeException()))
        Mockito.`when`(repository.getPhoneContacts())
                .thenReturn(Single.error(RuntimeException()))

        val testObserver = getAllContacts.buildUseCase(true).test()
        testObserver.awaitTerminalEvent()

        testObserver.assertNotComplete()
        testObserver.assertError(RuntimeException::class.java)
    }

    @Test
    fun getCharactersError() {
        Mockito.`when`(repository.getCharacters())
                .thenReturn(Single.error(RuntimeException()))

        val testObserver = getAllContacts.buildUseCase(false).test()
        testObserver.awaitTerminalEvent()

        testObserver.assertNotComplete()
        testObserver.assertError(RuntimeException::class.java)
    }
}