package com.vsantander.paymentchallenge.usecase

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.vsantander.paymentchallenge.data.repository.ContactRepositoryImpl
import com.vsantander.paymentchallenge.domain.usecase.GetAllSelectedContacts
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
class GetAllSelectedContactsTest {

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

    lateinit var getAllSelectedContacts: GetAllSelectedContacts

    @Before
    fun setUp() {
        getAllSelectedContacts = GetAllSelectedContacts(repository)
    }

    @Test
    fun getSelectedContactsOk() {
        val itemsList = ContactFactory.makeContactList(NUMBER_ITEMS)
        Mockito.`when`(repository.getAllSelectedContacts())
                .thenReturn(Single.just(itemsList))

        val testObserver = getAllSelectedContacts.buildUseCase().test()
        testObserver.awaitTerminalEvent()

        testObserver.assertComplete()
        testObserver.assertNoErrors()

        val result = testObserver.values()[0]
        assert(result == itemsList)
    }

    @Test
    fun getSelectedContactsError() {
        Mockito.`when`(repository.getAllSelectedContacts())
                .thenReturn(Single.error(RuntimeException()))

        val testObserver = getAllSelectedContacts.buildUseCase().test()
        testObserver.awaitTerminalEvent()

        testObserver.assertNotComplete()
        testObserver.assertError(RuntimeException::class.java)
    }
}