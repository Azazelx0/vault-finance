package com.example.vault.ui.util

import com.example.vault.domain.model.Currency
import java.text.DecimalFormat

object CurrencyFormatter {
    fun format(amount: Double, currencyCode: String = "USD"): String {
        val currency = Currency.fromCode(currencyCode)
        val formatter = DecimalFormat("#,##0.00")
        return "${currency.symbol}${formatter.format(amount)}"
    }
    
    // For cases where we just want the symbol
    fun getSymbol(currencyCode: String): String {
        return Currency.fromCode(currencyCode).symbol
    }
}
