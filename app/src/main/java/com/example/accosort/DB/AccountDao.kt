package com.example.accosort.DB

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
@Dao
interface AccountDao {
    @Query("SELECT * from Account")
    fun getAllAccount(): Flow<List<Account>>

    @Insert
    suspend fun addAccount(account: Account)

    @Delete
    suspend fun deleteAccount(account: Account)

    @Update
    suspend fun updateAccount(account: Account)

    @Query("SELECT * FROM Account WHERE id = :accountId")
    fun getAccountById(accountId: Int): Flow<Account?>

}