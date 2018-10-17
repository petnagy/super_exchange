package com.petnagy.superexchange.redux.action

import com.petnagy.koredux.Action
import com.petnagy.superexchange.data.HistoryRate

class LoadHistoryAction : HistoryAction()

class SetHistoryListAction(val historyList: List<HistoryRate>) : HistoryAction()

class HistoryErrorAction: HistoryAction()

open class HistoryAction : Action {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LastRateAction) return false
        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}