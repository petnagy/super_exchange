package com.petnagy.superexchange.redux.middleware

import com.petnagy.koredux.Action
import com.petnagy.koredux.DispatchFunction
import com.petnagy.koredux.Middleware
import com.petnagy.koredux.Store
import com.petnagy.superexchange.network.FixerIoEndpoint
import com.petnagy.superexchange.redux.action.LoadHistoryAction
import com.petnagy.superexchange.redux.state.AppState

class HistoryMiddleware(private val endpoint: FixerIoEndpoint): Middleware<AppState> {

    override fun invoke(store: Store<AppState>, action: Action, next: DispatchFunction) {
        when(action) {
            is LoadHistoryAction -> loadFromNet(store)
        }
    }

    private fun loadFromNet(store: Store<AppState>) {

    }

}