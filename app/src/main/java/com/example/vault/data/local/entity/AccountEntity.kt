package com.example.vault.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

enum class AccountType {
    CASH, BANK, CREDIT_CARD, WALLET, INVESTMENT, OTHER
}

@Entity(tableName = "accounts")
data class AccountEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val type: AccountType,
    val currencyCode: String = "USD",
    val currentBalance: Double,
    val initialBalance: Double,
    val colorHex: String,
    val iconName: String,
    val isArchived: Boolean = false,
    val lastUpdated: Long = System.currentTimeMillis()
)
