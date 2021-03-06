package com.petnagy.superexchange.convert

import com.petnagy.superexchange.data.HistoryRate
import com.petnagy.superexchange.data.LatestRate
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.math.BigDecimal
import java.math.RoundingMode

@RunWith(MockitoJUnitRunner::class)
class RateConverterTest {

    private lateinit var latestRate: LatestRate

    private lateinit var underTest: RateConverter

    @Before
    fun setUp() {
        val currencyMap: Map<String, BigDecimal> = mapOf("EUR" to BigDecimal(1), "HUF" to BigDecimal(320), "USD" to BigDecimal(1.1))
        latestRate = LatestRate(true, 1, "EUR", "date", currencyMap)
        underTest = RateConverter()
    }

    @Test
    fun testRateConverterWhenUserSelectedCurrencyNotAvailable() {
        // GIVEN
        // WHEN
        val result = underTest.convertLatestRate(latestRate, "WUT", 1)

        // THEN
        Assert.assertEquals(result.rates.size, 0)
    }

    @Test
    fun testRateConverterWhenUserSelectedCurrencySameAsOriginal() {
        // GIVEN
        // WHEN
        val result = underTest.convertLatestRate(latestRate, "EUR", 1)

        // THEN
        Assert.assertEquals(result.rates.size, 3)
        val eurValue = BigDecimal(1).divide(BigDecimal(1), 6, RoundingMode.CEILING)
        Assert.assertEquals(result.rates["EUR"], eurValue)
        val hufValue = BigDecimal(320).divide(BigDecimal(1), 6, RoundingMode.CEILING)
        Assert.assertEquals(result.rates["HUF"], hufValue)
        val usdValue = BigDecimal(1.1).divide(BigDecimal(1), 6, RoundingMode.CEILING)
        Assert.assertEquals(result.rates["USD"], usdValue)
    }

    @Test
    fun testRateConverterWhenUserSelectedCurrencyDifferentAsOriginal() {
        // GIVEN
        // WHEN
        val result = underTest.convertLatestRate(latestRate, "HUF", 1)

        // THEN
        Assert.assertEquals(result.rates.size, 3)
        val eurValue = BigDecimal(1).divide(BigDecimal(320), 6, RoundingMode.CEILING)
        Assert.assertEquals(result.rates["EUR"], eurValue)
        val hufValue = BigDecimal(320).divide(BigDecimal(320), 6, RoundingMode.CEILING)
        Assert.assertEquals(result.rates["HUF"], hufValue)
        val usdValue = BigDecimal(1.1).divide(BigDecimal(320), 6, RoundingMode.CEILING)
        Assert.assertEquals(result.rates["USD"], usdValue)
    }

    @Test
    fun testRateConverterWhenUserAddAnAmount() {
        // GIVEN
        // WHEN
        val result = underTest.convertLatestRate(latestRate, "HUF", 112)

        // THEN
        Assert.assertEquals(result.rates.size, 3)
        val eurValue = BigDecimal(1).divide(BigDecimal(320), 6, RoundingMode.CEILING) * BigDecimal(112)
        Assert.assertEquals(result.rates["EUR"], eurValue)
        val hufValue = BigDecimal(320).divide(BigDecimal(320), 6, RoundingMode.CEILING) * BigDecimal(112)
        Assert.assertEquals(result.rates["HUF"], hufValue)
        val usdValue = BigDecimal(1.1).divide(BigDecimal(320), 6, RoundingMode.CEILING) * BigDecimal(112)
        Assert.assertEquals(result.rates["USD"], usdValue)
    }

    @Test
    fun testConvertRatesToStringWithEmptyMap() {
        // GIVEN
        // WHEN
        val result = underTest.convertRatesToString(emptyMap())

        // THEN
        Assert.assertEquals(result, "")
    }

    @Test
    fun testConvertRatesToStringWithNoneEmptyMap() {
        // GIVEN
        // WHEN
        val rates = mapOf("HUF" to BigDecimal("0.123456"), "EUR" to BigDecimal("123.000123"), "USD" to BigDecimal("1"))
        val result = underTest.convertRatesToString(rates)

        // THEN
        val resultString = "HUF 0.123456\nEUR 123.000123\nUSD 1"
        Assert.assertEquals(result, resultString)
    }

    @Test
    fun testConvertHistoryItems() {
        // GIVEN
        val rate = HistoryRate(true, true, "date1", 1, "EUR", mapOf("HUF" to BigDecimal("320"), "EUR" to BigDecimal("1")))
        val historyItemList = listOf(rate)

        // WHEN
        val result = underTest.convertHistoryItems(historyItemList, "HUF")

        // THEN
        Assert.assertEquals(result.size, 1)
        Assert.assertEquals(result[0].rates["HUF"], BigDecimal("1.000000"))
        val convertedEUR = BigDecimal(1).divide(BigDecimal(320), 6, RoundingMode.CEILING)
        Assert.assertEquals(result[0].rates["EUR"], convertedEUR)
    }
}