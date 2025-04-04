package org.adastra.curriculum.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.adastra.curriculum.client.data.LoginRequest

@Composable
fun LoginScreen(loginViewModel: LoginViewModel) {
    MaterialTheme {
        var username by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var isPasswordVisible by remember { mutableStateOf(false) }
        var isLoading by remember { mutableStateOf(false) }
        var error by remember { mutableStateOf<String?>(null) }

        Column(Modifier.padding(16.dp)) {
            Text("Curriculum",
                style = MaterialTheme.typography.h3,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 20.dp, top = 50.dp).fillMaxWidth())
            TextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Uživatelské jméno") },
                modifier = Modifier.padding(bottom = 5.dp).fillMaxWidth())
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Heslo") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    // Eye icon to toggle password visibility
                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        Icon(
                            imageVector = if (isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = if (isPasswordVisible) "Hide password" else "Show password"
                        )
                    }
                })

            Button(
                colors = ButtonDefaults.buttonColors(Color.Blue),
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = {
                    isLoading = true
                    error = null
                    var loginRequest = LoginRequest(username, password, false)

                    CoroutineScope(Dispatchers.Main).launch {
                        if (loginViewModel.login(loginRequest)) {
                            // Login successful
                            isLoading = false
                        } else {
                            // Login failed
                            isLoading = false
                            error = "Přihlášení selhalo. Zkontrolujte uživatelské jméno a heslo."
                        }
                    }
                }) {
                    Text("Přihlásit", color = Color.White)
                }

                if (isLoading) {
                    CircularProgressIndicator(color = Color.Blue, modifier = Modifier.align(Alignment.CenterHorizontally))
                }

                error?.let {
                    Text(text = it, color = Color.Red, fontSize = TextUnit(12f, TextUnitType.Sp), modifier = Modifier.align(Alignment.CenterHorizontally))
                }
        }
    }
}