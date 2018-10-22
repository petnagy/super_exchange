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

    companion object {
        private const val SCALE = 6
    }

    fun convertLatestRate(latestRate: LatestRate, baseCurrency: String, amount: Int): LatestRate {
        val baseRate = latestRate.rates[baseCurrency]
        val convertedMap: Map<String, BigDecimal> = if (baseRate != null) {
            latestRate.rates.mapValues { entry ->
                convertLatestRateItem(entry.value, baseRate, amount)
            }
        } else {
            emptyMap()
        }
        return latestRate.copy(rates = convertedMap)
    }

    private fun convertLatestRateItem(entry: BigDecimal, baseRate: BigDecimal, amount: Int): BigDecimal {
        return entry.divide(baseRate, SCALE, RoundingMode.CEILING) * BigDecimal(amount)
    }

    fun convertHistoryItems(rates: List<HistoryRate>, baseCurrency: String): List<HistoryItemViewModel> {
        return rates.asSequence().map { historyRate -> convertToHistoryItemViewModel(historyRate, baseCurrency) }.toList()
    }

    private fun convertToHistoryItemViewModel(historyRate: HistoryRate, baseCurrency: String): HistoryItemViewModel {
        val date = historyRate.date
        val baseRate = historyRate.rates[baseCurrency]
        val convertedMap = if (baseRate != null) {
            historyRate.rates.mapValues { entry ->
                entry.value.divide(baseRate, SCALE, RoundingMode.CEILING)
            }
        } else {
            emptyMap<String, BigDecimal>()
        }
        val convertedMapAsString = convertedMap.map { entry -> entry.key + " " + entry.value.toString() }
                .joinToString("\n")
        return HistoryItemViewModel(date, convertedMapAsString)
    }
}
