package com.petnagy.superexchange.repository

/***
 * General Specification interface.
 */
interface Specification

/***
 * LatestRate related Specification.
 */
data class LatestRateSpecification(val symbols: String, val base: String, val date: String) : Specification

data class HistoryRateSpecification(val symbols: String, val base: String, val date: String) : Specification

class NoSpecification : Specification {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}
