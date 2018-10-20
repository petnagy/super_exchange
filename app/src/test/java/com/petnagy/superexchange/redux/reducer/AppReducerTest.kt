package com.petnagy.superexchange.redux.reducer

import com.petnagy.superexchange.data.Currency
import com.petnagy.superexchange.data.LatestRate
import com.petnagy.superexchange.location.LocationStatus
import com.petnagy.superexchange.permission.PermissionStatus
import com.petnagy.superexchange.redux.action.CalculateRatesAction
import com.petnagy.superexchange.redux.action.LatestRateErrorAction
import com.petnagy.superexchange.redux.action.NetworkErrorAction
import com.petnagy.superexchange.redux.action.SetBaseCurrencyAction
import com.petnagy.superexchange.redux.action.SetLatestRateAction
import com.petnagy.superexchange.redux.action.StartLocationSearchAction
import com.petnagy.superexchange.redux.state.AppState
import com.petnagy.superexchange.redux.state.HistoryRateState
import com.petnagy.superexchange.redux.state.LatestRateState
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class AppReducerTest {

    private lateinit var underTest: AppReducer

    @Before
    fun setUp() {
        underTest = AppReducer()
    }

    @Test
    fun testWith_StartLocationSearchAction() {
        val permissionStatus = PermissionStatus.PERMISSION_GRANTED
        val latestRate = LatestRate(true, 1, "EUR", "date", emptyMap())
        val latestRateState = LatestRateState(latestRate = latestRate)
        val appState = AppState(latestRateState = latestRateState, historyRateState = HistoryRateState())
        val newState = underTest.invoke(StartLocationSearchAction(permissionStatus), appState)

        Assert.assertEquals(true, newState.latestRateState.loading)
        Assert.assertNull(newState.latestRateState.latestRate)
    }

    @Test
    fun testWith_LatestRateErrorAction() {
        val latestRateState = LatestRateState(loading = true)
        val appState = AppState(latestRateState = latestRateState, historyRateState = HistoryRateState())
        val newState = underTest.invoke(LatestRateErrorAction(LocationStatus.STATUS_UNKNOWN), appState)

        Assert.assertEquals(false, newState.latestRateState.loading)
        Assert.assertEquals(LocationStatus.STATUS_UNKNOWN, newState.latestRateState.status)
    }

    @Test
    fun testWith_SetBaseCurrencyAction() {
        val latestRateState = LatestRateState(loading = true)
        val appState = AppState(latestRateState = latestRateState, historyRateState = HistoryRateState())
        val newState = underTest.invoke(SetBaseCurrencyAction(Currency.HUF), appState)

        Assert.assertEquals(Currency.HUF, newState.latestRateState.baseCurrency)
        Assert.assertEquals(LocationStatus.STATUS_OK, newState.latestRateState.status)
        Assert.assertNull(newState.latestRateState.latestRate)
        Assert.assertEquals(1, newState.latestRateState.amount)
    }

    @Test
    fun testWith_SetLatestRateAction() {
        val latestRate = LatestRate(true, 1, "EUR", "date", emptyMap())
        val latestRateState = LatestRateState(loading = true)
        val appState = AppState(latestRateState = latestRateState, historyRateState = HistoryRateState())
        val newState = underTest.invoke(SetLatestRateAction(latestRate), appState)

        Assert.assertEquals(false, newState.latestRateState.loading)
        Assert.assertEquals(latestRate, newState.latestRateState.latestRate)
    }

    @Test
    fun testWith_NetworkErrorAction() {
        val latestRate = LatestRate(true, 1, "EUR", "date", emptyMap())
        val latestRateState = LatestRateState(latestRate = latestRate, status = LocationStatus.STATUS_OK)
        val appState = AppState(latestRateState = latestRateState, historyRateState = HistoryRateState())
        val newState = underTest.invoke(NetworkErrorAction(), appState)

        Assert.assertEquals(false, newState.latestRateState.loading)
        Assert.assertEquals(LocationStatus.NETWORK_ERROR, newState.latestRateState.status)
    }

    @Test
    fun testWith_CalculateRatesAction() {
        val latestRate = LatestRate(true, 1, "EUR", "date", emptyMap())
        val latestRateState = LatestRateState(latestRate = latestRate, amount = 1)
        val appState = AppState(latestRateState = latestRateState, historyRateState = HistoryRateState())
        val newState = underTest.invoke(CalculateRatesAction(123), appState)

        Assert.assertEquals(123, newState.latestRateState.amount)
    }
}