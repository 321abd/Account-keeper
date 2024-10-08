package com.example.accosort

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.accosort.DB.AccountDatabase
import com.example.accosort.DB.AccountNavHost
import com.example.accosort.ui.theme.AccoSortTheme
import kotlinx.coroutines.Dispatchers

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = Room
            .databaseBuilder(applicationContext, AccountDatabase::class.java,
                "account-db")
            .build()
        val accountViewModel = AccountViewModel(OfflineAccountRepository(db.AccountDao()),ioDispatcher = Dispatchers.IO)

        setContent {
            AccoSortTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AccountNavHost(accountViewModel, navController= rememberNavController())
                }
            }
        }
    }
}


