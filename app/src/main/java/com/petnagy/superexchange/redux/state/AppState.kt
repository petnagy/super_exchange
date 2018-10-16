package com.petnagy.superexchange.redux.state

/***
 * Appstate class to describe all state(s) in the app.
 */
data class AppState(val fragmentState: FragmentState = FragmentState.LATEST_RATE, val latestRateState: LatestRateState, val historyRateState: HistoryRateState)