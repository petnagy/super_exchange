package com.petnagy.superexchange.redux.reducer

import com.petnagy.koredux.Action
import com.petnagy.koredux.Reducer
import com.petnagy.superexchange.location.LocationStatus
import com.petnagy.superexchange.redux.action.*
import com.petnagy.superexchange.redux.state.AppState
import com.petnagy.superexchange.redux.state.FragmentState
import com.petnagy.superexchange.redux.state.LatestRateState

class AppReducer: Reducer<AppState> {
    override fun invoke(action: Action, state: AppState): AppState {
        return AppState(fragmentState = fragmentStateReducer(action, state.fragmentState),
                latestRateState = latestRateReducer(action, state.latestRateState))
    }

    private fun fragmentStateReducer(action: Action, oldFragmentState: FragmentState): FragmentState {
        var state = oldFragmentState
        when(action) {
            //TODO change fragment
        }
        return state
    }

    private fun latestRateReducer(action: Action, oldLatestRateState: LatestRateState): LatestRateState {
        var state = oldLatestRateState
        when (action) {
            is StartLocationSearchAction -> state = state.copy(loading = true, locationSearchState = LocationStatus.STATUS_OK)
            is LocationSearchErrorAction -> state = state.copy(loading = false, locationSearchState = LocationStatus.LOCATION_ERROR)
            is NoPlayServiceAction -> state = state.copy(loading = false, locationSearchState = LocationStatus.PLAY_SERVICE_ERROR)
            is CanAskPermissionAction -> state = state.copy(loading = false, locationSearchState = LocationStatus.PERMISSION_NEED)
            is PermissionDeniedAction -> state = state.copy(loading = false, locationSearchState = LocationStatus.PERMISSION_DENIED)
            is LocationSettingsErrorAction -> state = state.copy(loading = false, locationSearchState = LocationStatus.SETTING_ERROR)
        }
        return state
    }

}