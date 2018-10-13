package com.petnagy.superexchange.convert

import com.petnagy.superexchange.data.LatestRate
import com.petnagy.superexchange.pages.fragments.currentrate.viewmodel.CurrentRateItemViewModel
import timber.log.Timber
import java.math.RoundingMode

/***
 * Create conversion between rates.
 */
class RateConverter {

    fun convertLatestRate(latestRate: LatestRate, userSelectedRate: String): List<CurrentRateItemViewModel> {
        val baseRate = latestRate.rates[userSelectedRate]
        return if (baseRate != null) {
            val convertedMap = latestRate.rates.mapValues { entry ->
                entry.value.divide(baseRate, 6, RoundingMode.CEILING)
            }
            Timber.d("Converted map: $convertedMap")
            convertedMap.map { entry -> CurrentRateItemViewModel(entry.key, entry.value) }.toList()
        } else {
            emptyList()
        }
    }

}