package org.adastra.curriculum.form

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Checkbox
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import org.adastra.curriculum.client.data.AccountRequest
import org.adastra.curriculum.client.data.AccountResponse
import org.adastra.curriculum.client.data.Authority

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AccountFormDialog(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onSubmit: (AccountRequest) -> Unit,
    existingAccount: AccountResponse? = null
) {
    if (isVisible) {
        Dialog(onDismissRequest = onDismiss) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(5.dp),
                color = MaterialTheme.colors.background
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    var selectedLogin by remember { mutableStateOf(existingAccount?.login ?: "") }
                    var selectedFirstName by remember { mutableStateOf(existingAccount?.firstName ?: "") }
                    var selectedLastName by remember { mutableStateOf(existingAccount?.lastName ?: "") }
                    var selectedEmail by remember { mutableStateOf(existingAccount?.email ?: "") }
                    var selectedActivated by remember { mutableStateOf(existingAccount?.activated ?: true) }
                    val selectedAuthorities = remember {
                        mutableStateMapOf<Int, Boolean>().apply {
                            this[0] = existingAccount?.authorities?.any { it.displayName == Authority.ROLE_ADMIN.displayName } == true
                            this[1] = existingAccount?.authorities?.any { it.displayName == Authority.ROLE_USER.displayName } == true
                        }
                    }

                    OutlinedTextField(modifier = Modifier.fillMaxWidth(), value = selectedLogin, onValueChange = { selectedLogin = it }, label = { Text("Login:") })
                    OutlinedTextField(modifier = Modifier.fillMaxWidth(), value = selectedFirstName, onValueChange = { selectedFirstName = it }, label = { Text("Jméno:") })
                    OutlinedTextField(modifier = Modifier.fillMaxWidth(), value = selectedLastName, onValueChange = { selectedLastName = it }, label = { Text("Příjmení:") })
                    OutlinedTextField(modifier = Modifier.fillMaxWidth(), value = selectedEmail, onValueChange = { selectedEmail = it }, label = { Text("Email:") })

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Status:",
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        Checkbox(
                            checked = selectedActivated == true,
                            onCheckedChange = { checked ->
                                selectedActivated = checked
                            }
                        )
                        Text("Je uživatel aktivní?", modifier = Modifier.padding(start = 8.dp))
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Uživatelské role:",
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        Checkbox(
                            checked = selectedAuthorities[0] == true,
                            onCheckedChange = { checked ->
                                selectedAuthorities[0] = checked
                            }
                        )
                        Text("Administrátor", modifier = Modifier.padding(start = 8.dp))
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        Checkbox(
                            checked = selectedAuthorities[1] == true,
                            onCheckedChange = { checked ->
                                selectedAuthorities[1] = checked
                            }
                        )
                        Text("Uživatel", modifier = Modifier.padding(start = 8.dp))
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        if (selectedLogin != "" && selectedFirstName != "" && selectedLastName != "" && selectedEmail != "") {
                            Button(
                                colors = ButtonDefaults.buttonColors(Color.Blue),
                                onClick = {
                                    var auths: List<Authority> = emptyList()
                                    if (selectedAuthorities[0] == true) {
                                        auths = auths + Authority.ROLE_ADMIN
                                    }
                                    if (selectedAuthorities[1] == true) {
                                        auths = auths + Authority.ROLE_USER
                                    }
                                    val accountRequest = AccountRequest(
                                        id = existingAccount?.id,
                                        login = selectedLogin,
                                        firstName = selectedFirstName,
                                        lastName = selectedLastName,
                                        email = selectedEmail,
                                        activated = selectedActivated,
                                        langKey = "cs",
                                        authorities = auths
                                    )
                                    onSubmit(accountRequest)
                                    onDismiss()
                                }) {
                                Text(
                                    "Uložit",
                                    color = Color.White,
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                        Button(
                            colors = ButtonDefaults.buttonColors(Color.Gray),
                            onClick = onDismiss) {
                                Text("Zpět")
                        }
                    }
                }
            }
        }
    }
}
