package com.example.vault.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

enum class CategoryType {
    INCOME, EXPENSE, TRANSFER
}

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val type: CategoryType,
    val colorHex: String,
    val iconName: String,
    val parentId: String? = null, // For subcategories
    val budgetId: String? = null,
    val isSystemDefault: Boolean = false
)
