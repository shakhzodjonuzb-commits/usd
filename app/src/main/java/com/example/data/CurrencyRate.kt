package com.example.data

/**
 * Represents exchange rates for a single currency.
 */
data class CurrencyRate(
    val code: String,
    val name: String,
    val flag: String,
    val buyRate: Double,
    val saleRate: Double,
    val mbRate: Double
)
