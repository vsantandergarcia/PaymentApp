package com.vsantander.paymentchallenge.usecase

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.vsantander.paymentchallenge.data.repository.ContactRepositoryImpl
import com.vsantander.paymentchallenge.domain.usecase.GetNumberSelectedContact
import com.vsantander.paymentchallenge.utils.RxImmediateSchedulerRule
import io.reactivex.Flowable
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetNumberSelectedContactTest {

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

    lateinit var getNumberSelectedContact: GetNumberSelectedContact

    @Before
    fun setUp() {
        getNumberSelectedContact = GetNumberSelectedContact(repository)
    }

    @Test
    fun getNumberSelectedContactOk() {
        Mockito.`when`(repository.getSelectedContactsCount())
                .thenReturn(Flowable.just(NUMBER_ITEMS))

        val testObserver = getNumberSelectedContact.buildUseCase().test()
        testObserver.awaitTerminalEvent()

        testObserver.assertComplete()
        testObserver.assertNoErrors()

        val result = testObserver.values()[0]
        assert(result == NUMBER_ITEMS)
    }

    @Test
    fun getNumberSelectedContactError() {
        Mockito.`when`(repository.getSelectedContactsCount())
                .thenReturn(Flowable.error(RuntimeException()))

        val testObserver = getNumberSelectedContact.buildUseCase().test()
        testObserver.awaitTerminalEvent()

        testObserver.assertNotComplete()
        testObserver.assertError(RuntimeException::class.java)
    }
}