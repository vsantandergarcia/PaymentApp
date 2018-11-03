package com.vsantander.paymentchallenge.usecase

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.vsantander.paymentchallenge.data.repository.ContactRepositoryImpl
import com.vsantander.paymentchallenge.domain.usecase.DeleteAllSelectedContacts
import com.vsantander.paymentchallenge.utils.RxImmediateSchedulerRule
import io.reactivex.Completable
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DeleteAllSelectedContactsTest {

    @Suppress("unused")
    @get:Rule // used to make all live data calls sync
    val instantExecutor = InstantTaskExecutorRule()

    @Rule
    @JvmField
    val testSchedulerRule = RxImmediateSchedulerRule()

    @Mock
    lateinit var repository: ContactRepositoryImpl

    lateinit var deleteAllSelectedContacts: DeleteAllSelectedContacts

    @Before
    fun setUp() {
        deleteAllSelectedContacts = DeleteAllSelectedContacts(repository)
    }

    @Test
    fun deleteAllSelectedContactsComplete() {
        Mockito.`when`(repository.deleteAllSelectedContacts())
                .thenReturn(Completable.complete())

        val testObserver = deleteAllSelectedContacts.buildUseCase().test()
        testObserver.awaitTerminalEvent()

        testObserver.assertComplete()
        testObserver.assertNoErrors()
    }

    @Test
    fun deleteAllSelectedContactsError() {
        Mockito.`when`(repository.deleteAllSelectedContacts())
                .thenReturn(Completable.error(RuntimeException()))

        val testObserver = deleteAllSelectedContacts.buildUseCase().test()
        testObserver.awaitTerminalEvent()

        testObserver.assertNotComplete()
        testObserver.assertError(RuntimeException::class.java)
    }
}