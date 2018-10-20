package com.petnagy.superexchange.convert

import com.petnagy.superexchange.data.HistoryRate
import com.petnagy.superexchange.data.LatestRate
import com.petnagy.superexchange.pages.fragments.currentrate.viewmodel.CurrentRateItemViewModel
import com.petnagy.superexchange.pages.fragments.history.viewmodel.HistoryItemViewModel
import java.math.BigDecimal
import java.math.RoundingMode

/***
 * Create conversion between rates.
 */
class RateConverter {

    fun convertLatestRate(latestRate: LatestRate, baseCurrency: String, amount: Int): List<CurrentRateItemViewModel> {
        val baseRate = latestRate.rates[baseCurrency]
        return if (baseRate != null) {
            val convertedMap = latestRate.rates.mapValues { entry ->
                entry.value.divide(baseRate, 6, RoundingMode.CEILING)
            }
            convertedMap.map { entry -> CurrentRateItemViewModel(entry.key, entry.value * BigDecimal(amount)) }.toList()
        } else {
            emptyList()
        }
    }

    fun convertHistoryItems(rates: List<HistoryRate>, baseCurrency: String): List<HistoryItemViewModel> {
        return rates.map { historyRate -> convertToHistoryItemViewModel(historyRate, baseCurrency) }.toList()
    }

    private fun convertToHistoryItemViewModel(historyRate: HistoryRate, baseCurrency: String): HistoryItemViewModel {
        val date = historyRate.date
        val baseRate = historyRate.rates[baseCurrency]
        val convertedMap = if (baseRate != null) {
            historyRate.rates.mapValues { entry ->
                entry.value.divide(baseRate, 6, RoundingMode.CEILING)
            }
        } else {
            emptyMap<String, BigDecimal>()
        }
        val convertedMapAsString = convertedMap.map { entry -> entry.key + " " + entry.value.toString() }.joinToString("\n")
        return HistoryItemViewModel(date, convertedMapAsString)
    }
}