package com.petnagy.superexchange.redux.action

import com.petnagy.koredux.Action
import com.petnagy.superexchange.data.LatestRate

class SetLatestRateAction(val latestRate: LatestRate): Action
class NetworkErrorAction: Action