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
