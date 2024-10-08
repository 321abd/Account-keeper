package com.example.accosort

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.accosort.DB.Account
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class AccountViewModel(
    private val repository: AccountRepository,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    //account data flow to show it with list
    val accounts = repository.getAllAccountStream()

    fun addAccount(account:Account){
        viewModelScope.launch (ioDispatcher){
            repository.insertAccount(account)
        }
    }
    fun deleteAccount(account:Account){
        viewModelScope.launch (ioDispatcher){
            repository.deleteAccount(account)
        }
    }

    fun updateAccount(account: Account) {
        viewModelScope.launch(ioDispatcher) {
            repository.updateAccount(account)
        }
    }

    fun getAccountById(accountId: Int): Flow<Account?> {
        return repository.getAccountById(accountId)
    }

}