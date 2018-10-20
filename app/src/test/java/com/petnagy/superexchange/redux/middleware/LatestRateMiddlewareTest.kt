package com.petnagy.superexchange.redux.middleware

import com.nhaarman.mockitokotlin2.mock
import com.petnagy.koredux.DispatchFunction
import com.petnagy.koredux.Store
import com.petnagy.superexchange.RxImmediateSchedulerRule
import com.petnagy.superexchange.data.Currency
import com.petnagy.superexchange.data.LatestRate
import com.petnagy.superexchange.redux.action.CalculateRatesAction
import com.petnagy.superexchange.redux.action.NetworkErrorAction
import com.petnagy.superexchange.redux.action.SetBaseCurrencyAction
import com.petnagy.superexchange.redux.action.SetLatestRateAction
import com.petnagy.superexchange.redux.state.AppState
import com.petnagy.superexchange.repository.LatestRateSpecification
import com.petnagy.superexchange.repository.Repository
import io.reactivex.Maybe
import org.junit.Before
import org.junit.ClassRule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.text.SimpleDateFormat
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class LatestRateMiddlewareTest {

    private lateinit var underTest: LatestRateMiddleware

    private lateinit var mockedRepository: Repository<LatestRate>

    private lateinit var mockedStore: Store<AppState>

    private lateinit var mockedDispatch: DispatchFunction

    companion object {
        @ClassRule
        @JvmField
        val schedulers = RxImmediateSchedulerRule()
    }

    @Before
    fun setUp() {
        mockedDispatch = mock()
        mockedStore = mock()
        mockedRepository = mock()
        underTest = LatestRateMiddleware(mockedRepository)
    }

    @Test
    fun testMiddlewareWith_SetBaseCurrencyAction_Success() {
        //GIVEN
        val symbols = Currency.values().joinToString(",")
        val date = SimpleDateFormat("yyyy-MM-dd").format(Date())
        val latestRate = LatestRate(true, 1, "EUR", date, emptyMap())
        Mockito.`when`(mockedRepository.load(LatestRateSpecification(symbols, "EUR", date))).thenReturn(Maybe.just(latestRate))

        //WHEN
        underTest.invoke(mockedStore, SetBaseCurrencyAction(Currency.USD), mockedDispatch)

        //THEN
        Mockito.verify(mockedStore).dispatch(SetLatestRateAction(latestRate))
    }

    @Test
    fun testMiddlewareWith_SetBaseCurrencyAction_Failed() {
        //GIVEN
        val symbols = Currency.values().joinToString(",")
        val date = SimpleDateFormat("yyyy-MM-dd").format(Date())
        Mockito.`when`(mockedRepository.load(LatestRateSpecification(symbols, "EUR", date))).thenReturn(Maybe.error(RuntimeException()))

        //WHEN
        underTest.invoke(mockedStore, SetBaseCurrencyAction(Currency.USD), mockedDispatch)

        //THEN
        Mockito.verify(mockedStore).dispatch(NetworkErrorAction())
    }

    @Test
    fun testMiddlewareWith_CalculateRatesAction_Success() {
        //GIVEN
        val symbols = Currency.values().joinToString(",")
        val date = SimpleDateFormat("yyyy-MM-dd").format(Date())
        val latestRate = LatestRate(true, 1, "EUR", date, emptyMap())
        Mockito.`when`(mockedRepository.load(LatestRateSpecification(symbols, "EUR", date))).thenReturn(Maybe.just(latestRate))

        //WHEN
        underTest.invoke(mockedStore, CalculateRatesAction(10), mockedDispatch)

        //THEN
        Mockito.verify(mockedStore).dispatch(SetLatestRateAction(latestRate))
    }

    @Test
    fun testMiddlewareWith_CalculateRatesAction_Failed() {
        //GIVEN
        val symbols = Currency.values().joinToString(",")
        val date = SimpleDateFormat("yyyy-MM-dd").format(Date())
        Mockito.`when`(mockedRepository.load(LatestRateSpecification(symbols, "EUR", date))).thenReturn(Maybe.error(RuntimeException()))

        //WHEN
        underTest.invoke(mockedStore, CalculateRatesAction(10), mockedDispatch)

        //THEN
        Mockito.verify(mockedStore).dispatch(NetworkErrorAction())
    }
}