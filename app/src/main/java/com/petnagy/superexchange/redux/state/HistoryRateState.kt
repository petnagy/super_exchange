package com.petnagy.superexchange.redux.state

import com.petnagy.superexchange.data.Currency
import com.petnagy.superexchange.data.HistoryRate

/***
 * History's view and app state(s).
 */
data class HistoryRateState(val loading: Boolean = false, val rates: List<HistoryRate>? = null, val baseCurrency: Currency? = null)
