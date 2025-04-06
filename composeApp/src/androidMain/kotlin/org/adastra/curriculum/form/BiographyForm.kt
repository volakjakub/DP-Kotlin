package org.adastra.curriculum.form

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalDate
import org.adastra.curriculum.client.data.AccountResponse
import org.adastra.curriculum.client.data.BiographyRequest
import org.adastra.curriculum.client.data.BiographyResponse
import org.adastra.curriculum.client.data.BiographyUserRequest
import org.adastra.curriculum.service.BiographyService
import java.time.Instant
import java.time.ZoneId

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BiographyForm(biographyService: BiographyService, biography: BiographyResponse?, account: AccountResponse, onFormSubmit: (BiographyRequest) -> Unit, onFormClose: () -> Unit) {
    var title by remember { mutableStateOf(biography?.title ?: "") }
    var firstName by remember { mutableStateOf(biography?.firstName ?: "") }
    var lastName by remember { mutableStateOf(biography?.lastName ?: "") }
    var phone by remember { mutableStateOf(biography?.phone ?: "") }
    var email by remember { mutableStateOf(biography?.email ?: "") }
    var street by remember { mutableStateOf(biography?.street ?: "") }
    var city by remember { mutableStateOf(biography?.city ?: "") }
    var country by remember { mutableStateOf(biography?.country ?: "") }
    var position by remember { mutableStateOf(biography?.position ?: "") }
    var employedFrom by remember { mutableStateOf(biography?.employedFrom ?: "") }
    val datePickerState = rememberDatePickerState()
    val openDatePicker = remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(12.dp).fillMaxWidth()) {
        OutlinedTextField(modifier = Modifier.fillMaxWidth(), value = title, onValueChange = { title = it }, label = { Text("Titul:") })
        OutlinedTextField(modifier = Modifier.fillMaxWidth(), value = firstName, onValueChange = { firstName = it }, label = { Text("Jméno:") })
        OutlinedTextField(modifier = Modifier.fillMaxWidth(), value = lastName, onValueChange = { lastName = it }, label = { Text("Příjmení:") })
        OutlinedTextField(modifier = Modifier.fillMaxWidth(), value = phone, onValueChange = { phone = it }, label = { Text("Telefon:") })
        OutlinedTextField(modifier = Modifier.fillMaxWidth(), value = email, onValueChange = { email = it }, label = { Text("E-mail:") })
        OutlinedTextField(modifier = Modifier.fillMaxWidth(), value = street, onValueChange = { street = it }, label = { Text("Ulice:") })
        OutlinedTextField(modifier = Modifier.fillMaxWidth(), value = city, onValueChange = { city = it }, label = { Text("Město:") })
        OutlinedTextField(modifier = Modifier.fillMaxWidth(), value = country, onValueChange = { country = it }, label = { Text("Stát:") })
        OutlinedTextField(modifier = Modifier.fillMaxWidth(), value = position, onValueChange = { position = it }, label = { Text("Pozice:") })
        if (employedFrom != "") {
            val employedFromDate = LocalDate.parse(employedFrom)
            val employedFromFormatted = "%02d.%02d.%04d".format(
                employedFromDate.dayOfMonth,
                employedFromDate.monthNumber,
                employedFromDate.year
            )
            Text(text = "Zaměstnán/a od: $employedFromFormatted", modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp))
        } else {
            Text(text = "Zaměstnán/a od: -", modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp))
        }
        Button(modifier = Modifier.fillMaxWidth().padding(), colors = ButtonDefaults.buttonColors(Color.Blue), onClick = { openDatePicker.value = true }) {
            Text(color = Color.White, text = "Vybrat datum")
        }

        if (openDatePicker.value) {
            DatePickerDialog(
                onDismissRequest = { openDatePicker.value = false },
                confirmButton = {
                    TextButton(onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val date = Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate()
                            //employedFrom = "%02d.%02d.%04d".format(date.dayOfMonth, date.monthValue, date.year)
                            employedFrom = "%04d-%02d-%02d".format(date.year, date.monthValue, date.dayOfMonth)
                        }
                        openDatePicker.value = false
                    }) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { openDatePicker.value = false }) {
                        Text("Zrušit")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }

        Button(
            modifier = Modifier.fillMaxWidth().padding(),
            colors = ButtonDefaults.buttonColors(Color.Blue),
            onClick = {
                val request = BiographyRequest(
                    id = biography?.id ?: 0,
                    title = title.ifBlank { null },
                    firstName = firstName,
                    lastName = lastName,
                    phone = phone,
                    email = email,
                    street = street,
                    city = city,
                    country = country,
                    position = position,
                    employedFrom = employedFrom,
                    user = BiographyUserRequest(
                        id = account.id,
                        login = account.login
                    )
                )
                onFormSubmit(request)
            }) {
                Text("Uložit", color = Color.White)
        }

        if (biography != null) {
            Button(
                modifier = Modifier.fillMaxWidth().padding(),
                colors = ButtonDefaults.buttonColors(Color.Gray),
                onClick = {
                    onFormClose()
                }) {
                Text("Zpět", color = Color.Black)
            }
        }
    }
}