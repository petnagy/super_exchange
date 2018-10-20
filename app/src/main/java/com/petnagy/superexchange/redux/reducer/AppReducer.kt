package com.petnagy.superexchange.redux.reducer

import com.petnagy.koredux.Action
import com.petnagy.koredux.Reducer
import com.petnagy.superexchange.data.Currency
import com.petnagy.superexchange.location.LocationStatus
import com.petnagy.superexchange.redux.action.*
import com.petnagy.superexchange.redux.state.AppState
import com.petnagy.superexchange.redux.state.FragmentState
import com.petnagy.superexchange.redux.state.HistoryRateState
import com.petnagy.superexchange.redux.state.LatestRateState

class AppReducer : Reducer<AppState> {
    override fun invoke(action: Action, state: AppState): AppState {
        return AppState(fragmentState = fragmentStateReducer(action, state.fragmentState),
                latestRateState = latestRateReducer(action, state.latestRateState),
                historyRateState = historyRateReducer(action, state.historyRateState, state.latestRateState.baseCurrency))
    }

    private fun fragmentStateReducer(action: Action, oldFragmentState: FragmentState): FragmentState {
        var state = oldFragmentState
        when (action) {
            is FragmentSwitchAction -> state = action.fragmentName
        }
        return state
    }

    private fun latestRateReducer(action: Action, oldLatestRateState: LatestRateState): LatestRateState {
        var state = oldLatestRateState
        when (action) {
            is StartLocationSearchAction -> state = state.copy(loading = true, latestRate = null)
            is LatestRateErrorAction -> state = state.copy(loading = false, status = action.status)
            is SetBaseCurrencyAction -> state = state.copy(baseCurrency = action.baseCurrency, latestRate = null, status = LocationStatus.STATUS_OK, amount = 1)
            is SetLatestRateAction -> state = state.copy(loading = false, latestRate = action.latestRate)
            is NetworkErrorAction -> state = state.copy(loading = false, status = LocationStatus.NETWORK_ERROR)
            is CalculateRatesAction -> state = state.copy(amount = action.amount)
        }
        return state
    }

    private fun historyRateReducer(action: Action, oldHistoryRateState: HistoryRateState, baseCurrency: Currency?): HistoryRateState {
        var state = oldHistoryRateState
        when (action) {
            is FragmentSwitchAction -> {
                if (action.fragmentName == FragmentState.HISTORY) {
                    state = state.copy(baseCurrency = baseCurrency)
                }
            }
            is LoadHistoryAction -> state = state.copy(loading = true, rates = null)
            is SetHistoryListAction -> state = state.copy(loading = false, rates = action.historyList)
            is HistoryErrorAction -> state = state.copy(loading = false, rates = null)
            is BaseCurrencyChangedAction -> state = state.copy(baseCurrency = action.selectedCurrency)
        }
        return state
    }

}