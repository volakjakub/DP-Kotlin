package org.adastra.curriculum

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.adastra.curriculum.auth.LoginViewModel
import org.adastra.curriculum.client.data.Authority

@Composable
fun App(loginViewModel: LoginViewModel) {
    MaterialTheme {
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Curriculum",
                style = MaterialTheme.typography.h3,
                textAlign = TextAlign.Center,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 20.dp, top = 30.dp).fillMaxWidth())

            Box(
                modifier = Modifier
                    .height(75.dp)
                    .background(Color.LightGray),
                contentAlignment = Alignment.BottomCenter
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Login: ${loginViewModel.getAccount()?.login}", fontSize = 15.sp, color = Color.Black)
                        if (loginViewModel.getAccount()?.authorities != null && loginViewModel.getAccount()?.authorities?.contains(
                                Authority.ROLE_ADMIN) == true
                        ) {
                            Text(
                                "Administrátor",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        } else {
                            Text(
                                "Uživatel",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }
                    }

                    Button(
                        colors = ButtonDefaults.buttonColors(Color.Red),
                        onClick = {
                            loginViewModel.logout()
                        }) {
                        Text("Odhlásit")
                    }
                }
            }
        }
    }
}