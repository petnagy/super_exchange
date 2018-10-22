package com.petnagy.superexchange.convert

import com.petnagy.superexchange.data.HistoryRate
import com.petnagy.superexchange.data.LatestRate
import java.math.BigDecimal
import java.math.RoundingMode

/***
 * Create conversion between rates.
 */
class RateConverter {

    companion object {
        private const val SCALE = 6
        private const val LINE_BREAK = "\n"
    }

    fun convertLatestRate(latestRate: LatestRate, baseCurrency: String, amount: Int): LatestRate {
        val convertedMap: Map<String, BigDecimal> = convertRates(latestRate.rates, baseCurrency, amount)
        return latestRate.copy(rates = convertedMap)
    }

    private fun convertLatestRateItem(entry: BigDecimal, baseRate: BigDecimal, amount: Int): BigDecimal {
        return entry.divide(baseRate, SCALE, RoundingMode.CEILING) * BigDecimal(amount)
    }

    fun convertHistoryItems(rates: List<HistoryRate>, baseCurrency: String): List<HistoryRate> {
        return rates.asSequence().map { historyRate -> convertHistoryRate(historyRate, baseCurrency) }.toList()
    }

    private fun convertHistoryRate(historyRate: HistoryRate, baseCurrency: String): HistoryRate {
        val convertedMap: Map<String, BigDecimal> = convertRates(historyRate.rates, baseCurrency, 1)
        return historyRate.copy(rates = convertedMap)
    }

    private fun convertRates(rates: Map<String, BigDecimal>, baseCurrency: String, amount: Int): Map<String, BigDecimal> {
        val baseRate = rates[baseCurrency]
        return if (baseRate != null) {
            rates.mapValues { entry ->
                convertLatestRateItem(entry.value, baseRate, amount)
            }
        } else {
            emptyMap()
        }
    }

    fun convertRatesToString(rates: Map<String, BigDecimal>): String {
        return rates.map { entry -> "${entry.key} ${entry.value}" }.joinToString(separator = LINE_BREAK)
    }
}
