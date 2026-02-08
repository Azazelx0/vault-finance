package com.example.vault.domain.model

import com.example.vault.data.local.entity.TransactionType
import java.util.Date

data class Transaction(
    val id: String,
    val amount: Double,
    val type: TransactionType,
    val accountId: String,
    val accountName: String, // Flattened for UI convenience
    val categoryId: String?, 
    val categoryName: String?,
    val categoryColor: String?, 
    val categoryIcon: String?,
    val date: Date,
    val note: String?
)
