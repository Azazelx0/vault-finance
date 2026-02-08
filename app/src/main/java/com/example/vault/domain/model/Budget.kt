package com.example.vault.domain.model

import com.example.vault.data.local.entity.BudgetPeriod
import java.util.Date

data class Budget(
    val id: String,
    val categoryId: String,
    val categoryName: String,
    val limitAmount: Double,
    val spentAmount: Double,
    val period: BudgetPeriod,
    val startDate: Date,
    val endDate: Date?,
    val notifyThresholdPercent: Int
) {
    val progressPercent: Float
        get() = if (limitAmount > 0) (spentAmount / limitAmount * 100).toFloat() else 0f
    
    val remaining: Double
        get() = limitAmount - spentAmount
    
    val isOverBudget: Boolean
        get() = spentAmount > limitAmount
    
    val isNearThreshold: Boolean
        get() = progressPercent >= notifyThresholdPercent
}
