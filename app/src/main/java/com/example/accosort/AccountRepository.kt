package com.example.accosort

import com.example.accosort.DB.Account
import com.example.accosort.DB.AccountDao
import kotlinx.coroutines.flow.Flow

interface AccountRepository{
    fun getAllAccountStream(): Flow<List<Account>>
    suspend fun insertAccount(account: Account)
    suspend fun deleteAccount(account: Account)
    suspend fun updateAccount(account: Account)
    fun getAccountById(accountId: Int): Flow<Account?>
}

class OfflineAccountRepository(private val accountDao : AccountDao):AccountRepository{
    override fun getAllAccountStream(): Flow<List<Account>> {
        return accountDao.getAllAccount()
    }

    override suspend fun insertAccount(account: Account) {
        accountDao.addAccount(account)
    }

    override suspend fun deleteAccount(account: Account) {
        accountDao.deleteAccount(account)
    }
    override suspend fun updateAccount(account: Account) {
        accountDao.updateAccount(account)
    }
    override fun getAccountById(accountId: Int): Flow<Account?> {
        return accountDao.getAccountById(accountId)
    }



}