package com.petnagy.superexchange.data

/***
 * Country enum to know currencies.
 */
enum class Country(val currency: Currency) {

    // https://www.nationsonline.org/oneworld/country_code_list.htm

    AT(Currency.EUR),
    BE(Currency.EUR),
    CA(Currency.CAD),
    CZ(Currency.CZK),
    HR(Currency.HRK),
    HU(Currency.HUF),
    DK(Currency.EUR),
    EE(Currency.EUR),
    FI(Currency.EUR),
    FR(Currency.EUR),
    DE(Currency.EUR),
    GR(Currency.EUR),
    UK(Currency.GBP);

    companion object {
        fun checkCountryCodeSupported(countryCode: String): Boolean {
            val validatedCountry = values().map { country -> country.name }.find { value -> value == countryCode }
            return validatedCountry != null
        }
    }
}
