package com.petnagy.superexchange.redux.middleware

import com.petnagy.koredux.Action
import com.petnagy.koredux.DispatchFunction
import com.petnagy.koredux.Middleware
import com.petnagy.koredux.Store
import com.petnagy.superexchange.redux.state.AppState
import timber.log.Timber

class LoggingMiddleware: Middleware<AppState> {

    override fun invoke(store: Store<AppState>, action: Action, next: DispatchFunction) {
        var log = "Action: -> " + action::class.java.simpleName
        Timber.d(log)
        next.dispatch(action)
    }

}