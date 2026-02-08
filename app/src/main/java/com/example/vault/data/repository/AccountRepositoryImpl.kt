package com.example.vault.data.repository

import com.example.vault.data.local.dao.AccountDao
import com.example.vault.data.local.entity.AccountEntity
import com.example.vault.data.local.entity.AccountType
import com.example.vault.domain.model.Account
import com.example.vault.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    private val dao: AccountDao
) : AccountRepository {

    override fun getAccounts(): Flow<List<Account>> {
        return dao.getAllAccounts().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getAccount(id: String): Account? {
        return dao.getAccountById(id)?.toDomain()
    }

    override suspend fun createAccount(name: String, type: String, initialBalance: Double, color: String, icon: String, currencyCode: String) {
        val entity = AccountEntity(
            name = name,
            type = AccountType.valueOf(type),
            initialBalance = initialBalance,
            currentBalance = initialBalance,
            colorHex = color,
            iconName = icon,
            currencyCode = currencyCode
        )
        dao.insertAccount(entity)
    }

    override suspend fun updateAccountBalance(id: String, newBalance: Double) {
        dao.updateBalance(id, newBalance)
    }

    private fun AccountEntity.toDomain(): Account {
        return Account(
            id = id,
            name = name,
            type = type,
            balance = currentBalance,
            currencyCode = currencyCode,
            colorHex = colorHex,
            iconName = iconName
        )
    }
}
