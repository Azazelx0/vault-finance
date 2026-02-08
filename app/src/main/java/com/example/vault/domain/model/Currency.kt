package com.example.vault.domain.model

enum class Currency(val code: String, val symbol: String, val displayName: String, val flag: String) {
    USD("USD", "$", "US Dollar", "🇺🇸"),
    EUR("EUR", "€", "Euro", "🇪🇺"),
    GBP("GBP", "£", "British Pound", "🇬🇧"),
    INR("INR", "₹", "Indian Rupee", "🇮🇳"),
    JPY("JPY", "¥", "Japanese Yen", "🇯🇵"),
    CAD("CAD", "C$", "Canadian Dollar", "🇨🇦"),
    AUD("AUD", "A$", "Australian Dollar", "🇦🇺"),
    CHF("CHF", "Fr", "Swiss Franc", "🇨🇭"),
    CNY("CNY", "¥", "Chinese Yuan", "🇨🇳"),
    NZD("NZD", "$", "New Zealand Dollar", "🇳🇿");

    companion object {
        fun fromCode(code: String): Currency = values().find { it.code == code } ?: USD
    }
}
