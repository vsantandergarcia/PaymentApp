package com.vsantander.paymentchallenge.usecase

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.vsantander.paymentchallenge.data.repository.ContactRepositoryImpl
import com.vsantander.paymentchallenge.domain.usecase.SaveSelectedContact
import com.vsantander.paymentchallenge.utils.RxImmediateSchedulerRule
import com.vsantander.paymentchallenge.utils.factory.ContactFactory
import io.reactivex.Completable
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SaveSelectedContactTest {

    @Suppress("unused")
    @get:Rule // used to make all live data calls sync
    val instantExecutor = InstantTaskExecutorRule()

    @Rule
    @JvmField
    val testSchedulerRule = RxImmediateSchedulerRule()

    @Mock
    lateinit var repository: ContactRepositoryImpl

    lateinit var saveSelectedContact: SaveSelectedContact

    @Before
    fun setUp() {
        saveSelectedContact = SaveSelectedContact(repository)
    }

    @Test
    fun saveSelectedContactOk() {
        val item = ContactFactory.makeContactModel()
        Mockito.`when`(repository.saveSelectedContact(item))
                .thenReturn(Completable.complete())

        val testObserver = saveSelectedContact.buildUseCase(item).test()
        testObserver.awaitTerminalEvent()

        testObserver.assertComplete()
        testObserver.assertNoErrors()
    }

    @Test
    fun saveSelectedContactError() {
        val item = ContactFactory.makeContactModel()
        Mockito.`when`(repository.saveSelectedContact(item))
                .thenReturn(Completable.error(RuntimeException()))

        val testObserver = saveSelectedContact.buildUseCase(item).test()
        testObserver.awaitTerminalEvent()

        testObserver.assertNotComplete()
        testObserver.assertError(RuntimeException::class.java)
    }
}