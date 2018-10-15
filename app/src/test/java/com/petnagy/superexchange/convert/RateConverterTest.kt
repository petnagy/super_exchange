package com.petnagy.superexchange.convert

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
        //GIVEN
        //WHEN
        val result = underTest.convertLatestRate(latestRate, "WUT", 1)

        //THEN
        Assert.assertEquals(result.size, 0)
    }

    @Test
    fun testRateConverterWhenUserSelectedCurrencySameAsOriginal() {
        //GIVEN
        //WHEN
        val result = underTest.convertLatestRate(latestRate, "EUR", 1)

        //THEN
        Assert.assertEquals(result.size, 3)
        val eurViewModel = result.find { itemViewModel -> itemViewModel.getCurrencyName() == "EUR" }
        val eurValue = BigDecimal(1).divide(BigDecimal(1), 6, RoundingMode.CEILING)
        Assert.assertEquals(eurViewModel?.getActualRate(), eurValue.toString())
        val hufViewModel = result.find { itemViewModel -> itemViewModel.getCurrencyName() == "HUF" }
        val hufValue = BigDecimal(320).divide(BigDecimal(1), 6, RoundingMode.CEILING)
        Assert.assertEquals(hufViewModel?.getActualRate(), hufValue.toString())
        val usdViewModel = result.find { itemViewModel -> itemViewModel.getCurrencyName() == "USD" }
        val usdValue = BigDecimal(1.1).divide(BigDecimal(1), 6, RoundingMode.CEILING)
        Assert.assertEquals(usdViewModel?.getActualRate(), usdValue.toString())
    }

    @Test
    fun testRateConverterWhenUserSelectedCurrencyDifferentAsOriginal() {
        //GIVEN
        //WHEN
        val result = underTest.convertLatestRate(latestRate, "HUF", 1)

        //THEN
        Assert.assertEquals(result.size, 3)
        val eurViewModel = result.find { itemViewModel -> itemViewModel.getCurrencyName() == "EUR" }
        val eurValue = BigDecimal(1).divide(BigDecimal(320), 6, RoundingMode.CEILING)
        Assert.assertEquals(eurViewModel?.getActualRate(), eurValue.toString())
        val hufViewModel = result.find { itemViewModel -> itemViewModel.getCurrencyName() == "HUF" }
        val hufValue = BigDecimal(320).divide(BigDecimal(320), 6, RoundingMode.CEILING)
        Assert.assertEquals(hufViewModel?.getActualRate(), hufValue.toString())
        val usdViewModel = result.find { itemViewModel -> itemViewModel.getCurrencyName() == "USD" }
        val usdValue = BigDecimal(1.1).divide(BigDecimal(320), 6, RoundingMode.CEILING)
        Assert.assertEquals(usdViewModel?.getActualRate(), usdValue.toString())
    }

    @Test
    fun testRateConverterWhenUserAddAnAmount() {
        //GIVEN
        //WHEN
        val result = underTest.convertLatestRate(latestRate, "HUF", 112)

        //THEN
        Assert.assertEquals(result.size, 3)
        val eurViewModel = result.find { itemViewModel -> itemViewModel.getCurrencyName() == "EUR" }
        val eurValue = BigDecimal(1).divide(BigDecimal(320), 6, RoundingMode.CEILING) * BigDecimal(112)
        Assert.assertEquals(eurViewModel?.getActualRate(), eurValue.toString())
        val hufViewModel = result.find { itemViewModel -> itemViewModel.getCurrencyName() == "HUF" }
        val hufValue = BigDecimal(320).divide(BigDecimal(320), 6, RoundingMode.CEILING) * BigDecimal(112)
        Assert.assertEquals(hufViewModel?.getActualRate(), hufValue.toString())
        val usdViewModel = result.find { itemViewModel -> itemViewModel.getCurrencyName() == "USD" }
        val usdValue = BigDecimal(1.1).divide(BigDecimal(320), 6, RoundingMode.CEILING) * BigDecimal(112)
        Assert.assertEquals(usdViewModel?.getActualRate(), usdValue.toString())
    }

}