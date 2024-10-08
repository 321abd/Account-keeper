package com.example.accosort.DB

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.accosort.AccountViewModel

@Composable
fun AccountNavHost(
    accountViewModel: AccountViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(navController = navController, startDestination = "HS", modifier = modifier) {
        composable(route = "HS") {
            HomeScreen(navController = navController, accountViewModel)
        }
        composable(route = "addAccount") {
            AddAccountScreenPage(accountViewModel, onAddButtonClick = accountViewModel::addAccount)
        }
        composable(route = "showAccounts/{accountType}") { backStackEntry ->
            val accountType = backStackEntry.arguments?.getString("accountType") ?: ""
            ShowAccountsScreen(accountViewModel, accountType,navController)
        }
        composable(route = "editAccount/{accountId}") { backStackEntry ->
            val accountId = backStackEntry.arguments?.getString("accountId")?.toInt() ?: 0
            EditAccountScreen(accountId, accountViewModel, navController)
        }
    }
}

@Composable
fun HomeScreen(navController: NavController,  accountViewModel: AccountViewModel) {
    val accounts by accountViewModel.accounts.collectAsState(initial = emptyList())
    val accountTypes = accounts.map { it.accountType }.distinct()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            accountTypes.forEach { accountType ->
                Button(
                    onClick = { navController.navigate("showAccounts/$accountType") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = accountType)
                }
            }
        }

        FloatingActionButton(
            onClick = { navController.navigate("addAccount") },
            modifier = Modifier.align(Alignment.BottomEnd)
        ) {
            Text("+")
        }
    }
}

@Composable
fun AddAccountScreenPage(accountViewModel: AccountViewModel,onAddButtonClick: (Account) -> Unit = {}) {
    var name by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var accountType by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username/Gmail") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = accountType,
            onValueChange = { accountType = it },
            label = { Text("Account Type") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                val newAccount = Account(
                    name = name,
                    username = username,
                    accountType = accountType,
                    password = password
                )
                accountViewModel.addAccount(newAccount)
                name = ""
                username = ""
                password = ""
                accountType = ""
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(text = "Add Account")
        }
    }
}




@Composable
fun ShowAccountsScreen(accountViewModel: AccountViewModel, accountType: String, navController: NavController) {
    val accounts by accountViewModel.accounts.collectAsState(initial = emptyList())
    val filteredAccounts = accounts.filter { it.accountType == accountType }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(filteredAccounts) { account ->
            AccountCard(account = account, accountViewModel, navController)
        }
    }
}


@Composable
fun AccountCard(account: Account, accountViewModel: AccountViewModel, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Name: ${account.name}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Username: ${account.username}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Account Type: ${account.accountType}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Password: ${account.password}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Button(
                    onClick = { navController.navigate("editAccount/${account.id}") }
                ) {
                    Text(text = "Edit")
                }
            }
            IconButton(
                onClick = { accountViewModel.deleteAccount(account) },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Account",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}


@Composable
fun EditAccountScreen(
    accountId: Int,
    accountViewModel: AccountViewModel,
    navController: NavController
) {
    val accountFlow = accountViewModel.getAccountById(accountId)
    val account by accountFlow.collectAsState(initial = null)

    account?.let { account ->
        var name by remember { mutableStateOf(account.name) }
        var username by remember { mutableStateOf(account.username) }
        var password by remember { mutableStateOf(account.password) }
        var accountType by remember { mutableStateOf(account.accountType) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username/Gmail") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = accountType,
                onValueChange = { accountType = it },
                label = { Text("Account Type") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    val updatedAccount = account.copy(
                        name = name,
                        username = username,
                        accountType = accountType,
                        password = password
                    )
                    accountViewModel.updateAccount(updatedAccount)
                    navController.navigate("HS")
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(text = "Update Account")
            }
        }
    }
}