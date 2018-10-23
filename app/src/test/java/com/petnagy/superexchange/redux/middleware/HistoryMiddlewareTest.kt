package com.petnagy.superexchange.redux.middleware

import com.nhaarman.mockitokotlin2.mock
import com.petnagy.koredux.DispatchFunction
import com.petnagy.koredux.Store
import com.petnagy.superexchange.RxImmediateSchedulerRule
import com.petnagy.superexchange.data.HistoryRate
import com.petnagy.superexchange.redux.action.HistoryErrorAction
import com.petnagy.superexchange.redux.action.LoadHistoryAction
import com.petnagy.superexchange.redux.action.SetHistoryListAction
import com.petnagy.superexchange.redux.state.AppState
import com.petnagy.superexchange.repository.NoSpecification
import com.petnagy.superexchange.repository.Repository
import io.reactivex.Maybe
import org.junit.Before
import org.junit.ClassRule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.math.BigDecimal

@RunWith(MockitoJUnitRunner::class)
class HistoryMiddlewareTest {

    companion object {
        @ClassRule
        @JvmField
        val schedulers = RxImmediateSchedulerRule()
    }

    private lateinit var underTest: HistoryMiddleware

    private lateinit var mockedRepository: Repository<List<HistoryRate>>

    private lateinit var mockedStore: Store<AppState>

    private lateinit var mockedDispatchFunction: DispatchFunction

    @Before
    fun setUp() {
        mockedStore = mock()
        mockedDispatchFunction = mock()
        mockedRepository = mock()
        underTest = HistoryMiddleware(mockedRepository)
    }

    @Test
    fun testLoadHistoryActionFailed() {
        // GIVEN
        Mockito.`when`(mockedRepository.load(NoSpecification())).thenReturn(Maybe.error(RuntimeException()))

        // WHEN
        underTest.invoke(mockedStore, LoadHistoryAction(), mockedDispatchFunction)

        // THEN
        Mockito.verify(mockedStore).dispatch(HistoryErrorAction())
    }

    @Test
    fun testLoadHistoryActionSuccess() {
        // GIVEN
        val historyRate1 = HistoryRate(true, true, "date1", 1, "EUR", mapOf("EUR" to BigDecimal(1), "USD" to BigDecimal(2)))
        val historyRate2 = HistoryRate(true, true, "date2", 2, "EUR", mapOf("EUR" to BigDecimal(1), "USD" to BigDecimal(3)))
        val listOfResult = listOf(historyRate1, historyRate2)
        Mockito.`when`(mockedRepository.load(NoSpecification())).thenReturn(Maybe.just(listOfResult))

        // WHEN
        underTest.invoke(mockedStore, LoadHistoryAction(), mockedDispatchFunction)

        // THEN
        Mockito.verify(mockedStore).dispatch(SetHistoryListAction(listOfResult))
    }
}