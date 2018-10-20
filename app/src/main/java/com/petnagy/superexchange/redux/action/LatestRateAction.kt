package com.petnagy.superexchange.redux.action

import com.petnagy.koredux.Action
import com.petnagy.superexchange.data.LatestRate

class SetLatestRateAction(val latestRate: LatestRate) : LastRateAction()
class NetworkErrorAction : LastRateAction()
data class CalculateRatesAction(val amount: Int) : LastRateAction()

open class LastRateAction : Action {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LastRateAction) return false
        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}