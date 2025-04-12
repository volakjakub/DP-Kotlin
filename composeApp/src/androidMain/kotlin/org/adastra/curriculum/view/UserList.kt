package org.adastra.curriculum.view

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.launch
import org.adastra.curriculum.client.data.AccountRequest
import org.adastra.curriculum.client.data.AccountResponse
import org.adastra.curriculum.form.AccountFormDialog
import org.adastra.curriculum.service.BiographyService
import org.adastra.curriculum.service.UserService

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UserList(userService: UserService, biographyService: BiographyService, admin: AccountResponse) {
    var page by remember { mutableIntStateOf(0) }
    var size by remember { mutableIntStateOf(5) }
    var canLoadNext by remember { mutableStateOf(true) }
    var isLoadingAccounts by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var showBio by remember { mutableStateOf(false) }
    var showAccountForm by remember { mutableStateOf(false) }
    var accounts by remember { mutableStateOf<List<AccountResponse>>(emptyList()) }
    var accountEdit by remember { mutableStateOf<AccountResponse?>(null) }
    var accountBio by remember { mutableStateOf<AccountResponse?>(null) }
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        try {
            isLoadingAccounts = true
            accounts = userService.getUsers(page, size)
        } catch (e: Exception) {
            error = e.message
        } finally {
            isLoadingAccounts = false
        }
    }

    fun createAccount(created: AccountResponse) {
        accounts = accounts + created
    }

    fun updateAccount(updated: AccountResponse) {
        accounts = accounts.map { account ->
            if (account.id == updated.id) updated else account
        }
    }

    val onShowForm: (AccountResponse) -> Unit = { account ->
        accountEdit = account
        showAccountForm = true
    }

    val onFormSubmit: (AccountRequest) -> Unit = { request ->
        coroutineScope.launch {
            try {
                isLoadingAccounts = true
                if (request.id == null) {
                    createAccount(userService.createUser(request))
                } else {
                    updateAccount(userService.updateUser(request))
                }
            } catch (e: Exception) {
                error = e.message
            } finally {
                isLoadingAccounts = false
            }
        }
    }

    val onLoadNext: () -> Unit = {
        coroutineScope.launch {
            try {
                isLoadingAccounts = true
                val loadedAccounts = userService.getUsers(page + 1, size)
                accounts = accounts + loadedAccounts
                if (loadedAccounts.isEmpty()) {
                    canLoadNext = false
                }
            } catch (e: Exception) {
                error = e.message
            } finally {
                isLoadingAccounts = false
                page++
            }
        }
    }

    val onShowBio: (AccountResponse) -> Unit = { account ->
        accountBio = account
        showBio = true
    }

    if (showBio && accountBio != null) {
        Dialog(onDismissRequest = {
            showBio = false
        }) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(5.dp).verticalScroll(scrollState),
                color = MaterialTheme.colors.background
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
                        Button(
                            colors = ButtonDefaults.buttonColors(Color.Gray),
                            onClick = {
                                showBio = false
                            }) {
                            Text("Zavřít")
                        }
                    }
                    BiographyDetail(biographyService, accountBio!!, false)
                }
            }
        }
    }

    Text(
        "Uživatelé",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        style = MaterialTheme.typography.h5,
        textAlign = TextAlign.Left,
        color = Color.Black,
        modifier = Modifier.padding(bottom = 10.dp, top = 15.dp)
            .fillMaxWidth()
    )
    Button(
        colors = ButtonDefaults.buttonColors(Color.Blue),
        onClick = {
            accountEdit = null
            showAccountForm = true
        }) {
        Text(
            "Vytvořit",
            color = Color.White,
        )
    }

    if (showAccountForm) {
        AccountFormDialog(
            isVisible = showAccountForm,
            onDismiss = { showAccountForm = false },
            onSubmit = onFormSubmit,
            existingAccount = accountEdit
        )
    }

    if (error != null) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = error.toString(),
                color = Color.Red,
                fontSize = TextUnit(12f, TextUnitType.Sp),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
    if (isLoadingAccounts) {
        Column(modifier = Modifier.fillMaxWidth()) {
            CircularProgressIndicator(
                color = Color.Blue,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }

    when {
        accounts.isNotEmpty() -> {
            accounts.forEachIndexed { index, account ->
                UserBox(account, onShowForm, onShowBio, admin)
                if (index < accounts.lastIndex) {
                    Divider(
                        color = Color.Gray,
                        thickness = 1.dp,
                        modifier = Modifier.padding(vertical = 5.dp)
                    )
                }
            }
        }
    }

    if (canLoadNext) {
        Button(
            colors = ButtonDefaults.buttonColors(Color.Blue),
            onClick = onLoadNext
        ) {
            Text(
                "Další",
                color = Color.White,
            )
        }
    }
}