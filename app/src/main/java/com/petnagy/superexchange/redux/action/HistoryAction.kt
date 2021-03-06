package com.petnagy.superexchange.redux.action

import com.petnagy.koredux.Action
import com.petnagy.superexchange.data.Currency
import com.petnagy.superexchange.data.HistoryRate

class LoadHistoryAction : HistoryAction()

class SetHistoryListAction(val historyList: List<HistoryRate>) : HistoryAction()

class HistoryErrorAction : HistoryAction()

class BaseCurrencyChangedAction(val selectedCurrency: Currency) : HistoryAction()

open class HistoryAction : Action {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is HistoryAction) return false
        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}
