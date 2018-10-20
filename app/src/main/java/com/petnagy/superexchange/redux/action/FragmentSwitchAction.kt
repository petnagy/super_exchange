package com.petnagy.superexchange.redux.action

import com.petnagy.koredux.Action
import com.petnagy.superexchange.redux.state.FragmentState

class FragmentSwitchAction(val fragmentName: FragmentState) : FragmentChangeAction()

open class FragmentChangeAction : Action {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LastRateAction) return false
        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}
