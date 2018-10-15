package com.petnagy.superexchange.redux.middleware

import com.petnagy.koredux.DispatchFunction
import com.petnagy.koredux.Store
import com.petnagy.superexchange.data.Currency
import com.petnagy.superexchange.data.LatestRate
import com.petnagy.superexchange.pages.fragments.currentrate.model.RxImmediateSchedulerRule
import com.petnagy.superexchange.redux.action.SetBaseCurrencyAction
import com.petnagy.superexchange.redux.state.AppState
import com.petnagy.superexchange.repository.LatestRateSpecification
import com.petnagy.superexchange.repository.Repository
import io.reactivex.Maybe
import org.junit.Assert
import org.junit.Before
import org.junit.ClassRule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.text.SimpleDateFormat
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class LatestRateMiddlewareTest {

    private inline fun <reified T: Any> mock() = Mockito.mock(T::class.java)

    inline fun <reified T : Any> argumentCaptor() = ArgumentCaptor.forClass(T::class.java)

    private lateinit var underTest: LatestRateMiddleware

    private lateinit var mockedRepository: Repository<LatestRate>

    private lateinit var mockedStore: Store<AppState>

    private lateinit var mockedDispatch: DispatchFunction

    companion object {
        @ClassRule @JvmField
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
    fun testMiddlewareWithSetBaseCurrencyAction() {
        //GIVEN
        val symbols = Currency.values().joinToString { "," }
        val date = SimpleDateFormat("yyyy-MM-dd").format(Date())
        Mockito.`when`(mockedRepository.load(LatestRateSpecification(symbols, "EUR", date))).thenReturn(Maybe.just(LatestRate(true, 1, "EUR", date, emptyMap())))

        //WHEN
        underTest.invoke(mockedStore, SetBaseCurrencyAction(Currency.EUR), mockedDispatch)

        //THEN
        val captor = argumentCaptor<LatestRateSpecification>()
        Mockito.verify(mockedRepository).load(captor.capture())
        Assert.assertEquals(captor.value.symbols, Currency.values().joinToString { "," })
    }

}