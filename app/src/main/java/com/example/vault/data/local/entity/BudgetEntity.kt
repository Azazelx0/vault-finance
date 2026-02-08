package com.example.vault.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

enum class BudgetPeriod {
    WEEKLY, MONTHLY, YEARLY, CUSTOM
}

@Entity(
    tableName = "budgets",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("categoryId")]
)
data class BudgetEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val categoryId: String,
    val limitAmount: Double,
    val period: BudgetPeriod,
    val startDate: Long,
    val endDate: Long? = null, // Null = Indefinite/Recurring
    val notifyThresholdPercent: Int = 80 // Alert when 80% used
)
