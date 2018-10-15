package com.petnagy.superexchange.redux.state

import com.petnagy.superexchange.data.Currency
import com.petnagy.superexchange.data.LatestRate
import com.petnagy.superexchange.location.LatestRateStatus


/***
 * Latest Rate View State.
 */
data class LatestRateState(val baseCurrency: Currency? = null, val amount: Int = 1, val latestRate: LatestRate? = null,
                           val loading: Boolean = false, val status: LatestRateStatus = LatestRateStatus.STATUS_UNKNOWN)