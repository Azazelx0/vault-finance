package com.example.vault.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "goals")
data class GoalEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val targetAmount: Double,
    val currentSavedAmount: Double,
    val currencyCode: String = "USD",
    val targetDate: Long?, // Epoch millis
    val colorHex: String,
    val iconName: String,
    val isCompleted: Boolean = false,
    val notes: String? = null
)
