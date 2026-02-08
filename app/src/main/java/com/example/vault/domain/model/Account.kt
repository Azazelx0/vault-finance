package com.example.vault.domain.model

import com.example.vault.data.local.entity.AccountType

data class Account(
    val id: String,
    val name: String,
    val type: AccountType,
    val balance: Double,
    val currencyCode: String,
    val colorHex: String,
    val iconName: String
)
