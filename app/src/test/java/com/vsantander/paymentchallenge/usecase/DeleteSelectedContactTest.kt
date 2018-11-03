package com.vsantander.paymentchallenge.usecase

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.vsantander.paymentchallenge.data.repository.ContactRepositoryImpl
import com.vsantander.paymentchallenge.domain.usecase.DeleteSelectedContact
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
class DeleteSelectedContactTest {

    @Suppress("unused")
    @get:Rule // used to make all live data calls sync
    val instantExecutor = InstantTaskExecutorRule()

    @Rule
    @JvmField
    val testSchedulerRule = RxImmediateSchedulerRule()

    @Mock
    lateinit var repository: ContactRepositoryImpl

    lateinit var deleteSelectedContact: DeleteSelectedContact

    @Before
    fun setUp() {
        deleteSelectedContact = DeleteSelectedContact(repository)
    }

    @Test
    fun deleteSelectedContactComplete() {
        val item = ContactFactory.makeContactModel()
        Mockito.`when`(repository.deleteSelectedContact(item))
                .thenReturn(Completable.complete())

        val testObserver = deleteSelectedContact.buildUseCase(item).test()
        testObserver.awaitTerminalEvent()

        testObserver.assertComplete()
        testObserver.assertNoErrors()
    }

    @Test
    fun deleteSelectedContactError() {
        val item = ContactFactory.makeContactModel()
        Mockito.`when`(repository.deleteSelectedContact(item))
                .thenReturn(Completable.error(RuntimeException()))

        val testObserver = deleteSelectedContact.buildUseCase(item).test()
        testObserver.awaitTerminalEvent()

        testObserver.assertNotComplete()
        testObserver.assertError(RuntimeException::class.java)
    }
}