package org.adastra.curriculum.view

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.adastra.curriculum.client.data.AccountResponse
import org.adastra.curriculum.client.data.Authority

@Composable
fun UserBox(account: AccountResponse, onShowForm: (AccountResponse) -> Unit, onShowBio: (AccountResponse) -> Unit, admin: AccountResponse) {
    var isExpanded by remember { mutableStateOf(false) }

    fun translateAuthority(authority: Authority): String {
        if (authority == Authority.ROLE_ADMIN) {
            return "Administrátor"
        }
        if (authority == Authority.ROLE_USER) {
            return "Uživatel"
        }
        return "-"
    }

    Box(
        modifier = Modifier
            .background(Color.LightGray)
            .fillMaxWidth()
            .clickable {
                isExpanded = !isExpanded
            }
            .animateContentSize(tween(durationMillis = 300)),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(7.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        account.firstName + " " + account.lastName,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        "Login: " + account.login,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black
                    )
                    if (account.activated) {
                        Text(
                            "Status: aktivní",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.Black
                        )
                    } else {
                        Text(
                            "Status: neaktivní",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.Black
                        )
                    }
                    Text(
                        "Role: " + account.authorities.joinToString(", ") { skill -> translateAuthority(skill) },
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black
                    )
                }
            }
            if (isExpanded) {
                Divider(
                    color = Color.Black,
                    thickness = 1.dp,
                    modifier = Modifier.padding(vertical = 5.dp)
                )


                Row(
                    modifier = Modifier.fillMaxWidth().padding(7.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    if (account.authorities.contains(Authority.ROLE_ADMIN) != true) {
                        Button(
                            colors = ButtonDefaults.buttonColors(Color.Blue),
                            onClick = {
                                onShowBio(account)
                            }) {
                            Text(
                                "Životopis",
                                color = Color.White,
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    if (account.login != admin.login) {
                        Button(
                            colors = ButtonDefaults.buttonColors(Color.Blue),
                            onClick = {
                                onShowForm(account)
                            }) {
                            Text(
                                "Upravit",
                                color = Color.White,
                            )
                        }
                    }
                }
            }
        }
    }
}