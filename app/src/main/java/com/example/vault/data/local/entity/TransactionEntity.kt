package com.example.vault.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

enum class TransactionType {
    INCOME, EXPENSE, TRANSFER
}

enum class TransactionStatus {
    CLEARED, PENDING, RECONCILED
}

@Entity(
    tableName = "transactions",
    foreignKeys = [
        ForeignKey(
            entity = AccountEntity::class,
            parentColumns = ["id"],
            childColumns = ["accountId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index("accountId"),
        Index("categoryId"),
        Index("date")
    ]
)
data class TransactionEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val amount: Double,
    val type: TransactionType,
    
    val accountId: String,
    val toAccountId: String? = null, // For transfers
    
    val categoryId: String?, // Nullable for transfers
    
    val date: Long, // Epoch millis
    val note: String? = null,
    
    val status: TransactionStatus = TransactionStatus.CLEARED,
    
    // Tagging (Stored as JSON string or handled via separate Relation table - using String for simplicity in MVP)
    val tags: String = "", 
    
    val attachmentPath: String? = null
)
