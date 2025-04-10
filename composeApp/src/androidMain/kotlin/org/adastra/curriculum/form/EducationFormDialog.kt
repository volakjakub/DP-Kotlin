package org.adastra.curriculum.form

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
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
import androidx.compose.ui.window.Dialog
import kotlinx.datetime.LocalDate
import org.adastra.curriculum.client.data.Education
import org.adastra.curriculum.client.data.EducationRequest
import org.adastra.curriculum.client.data.EducationResponse
import org.adastra.curriculum.helper.EducationHelper
import java.time.Instant
import java.time.ZoneId

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun EducationFormDialog(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onSubmit: (EducationRequest) -> Unit,
    existingEducation: EducationResponse? = null
) {
    val educationHelper = EducationHelper()
    val openStartDatePicker = remember { mutableStateOf(false) }
    val openEndDatePicker = remember { mutableStateOf(false) }
    val starDatePickerState = rememberDatePickerState()
    val endDatePickerState = rememberDatePickerState()

    if (isVisible) {
        Dialog(onDismissRequest = onDismiss) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(5.dp),
                color = MaterialTheme.colors.background
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    var selectedSchool by remember { mutableStateOf(existingEducation?.school ?: "") }
                    var selectedType by remember { mutableStateOf(existingEducation?.type ?: "") }
                    var selectedStart by remember { mutableStateOf(existingEducation?.start ?: "") }
                    var selectedEnd by remember { mutableStateOf(existingEducation?.end ?: "") }

                    // Language Dropdown
                    var expandedType by remember { mutableStateOf(false) }
                    val typeOptions = listOf(Education.HIGH_SCHOOL, Education.BACHELOR, Education.MASTER, Education.DOCTORATE)

                    OutlinedTextField(modifier = Modifier.fillMaxWidth(), value = selectedSchool, onValueChange = { selectedSchool = it }, label = { Text("Škola:") })
                    ExposedDropdownMenuBox(
                        expanded = expandedType,
                        onExpandedChange = { expandedType = !expandedType },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = educationHelper.getType(selectedType),
                            onValueChange = {},
                            label = { Text("Typ vzdělání") },
                            readOnly = true,
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = null
                                )
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = expandedType,
                            onDismissRequest = { expandedType = false }
                        ) {
                            typeOptions.forEach { type ->
                                DropdownMenuItem(onClick = {
                                    selectedType = type.displayName
                                    expandedType = false
                                }) {
                                    Text(text = educationHelper.getType(type.displayName))
                                }
                            }
                        }
                    }

                    if (selectedStart != "") {
                        val startDate = LocalDate.parse(selectedStart)
                        val startDateFormatted = "%02d.%02d.%04d".format(
                            startDate.dayOfMonth,
                            startDate.monthNumber,
                            startDate.year
                        )
                        Text(
                            text = "Počátek studia: $startDateFormatted",
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                        )
                    } else {
                        Text(
                            text = "Počátek studia: -",
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                        )
                    }
                    Button(modifier = Modifier.fillMaxWidth().padding(), colors = ButtonDefaults.buttonColors(Color.Blue), onClick = { openStartDatePicker.value = true }) {
                        Text(color = Color.White, text = "Vybrat datum počátku")
                    }

                    if (openStartDatePicker.value) {
                        DatePickerDialog(
                            onDismissRequest = { openStartDatePicker.value = false },
                            confirmButton = {
                                TextButton(onClick = {
                                    starDatePickerState.selectedDateMillis?.let { millis ->
                                        val date = Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate()
                                        selectedStart = "%04d-%02d-%02d".format(date.year, date.monthValue, date.dayOfMonth)
                                    }
                                    openStartDatePicker.value = false
                                }) {
                                    Text("OK")
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = { openStartDatePicker.value = false }) {
                                    Text("Zrušit")
                                }
                            }
                        ) {
                            DatePicker(state = starDatePickerState)
                        }
                    }

                    if (selectedEnd != "") {
                        val endDate = LocalDate.parse(selectedEnd)
                        val endDateFormatted = "%02d.%02d.%04d".format(
                            endDate.dayOfMonth,
                            endDate.monthNumber,
                            endDate.year
                        )
                        Text(
                            text = "Ukončení studia: $endDateFormatted",
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                        )
                    } else {
                        Text(
                            text = "Ukončení studia: -",
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                        )
                    }
                    Button(modifier = Modifier.fillMaxWidth().padding(), colors = ButtonDefaults.buttonColors(Color.Blue), onClick = { openEndDatePicker.value = true }) {
                        Text(color = Color.White, text = "Vybrat datum ukončení")
                    }

                    if (openEndDatePicker.value) {
                        DatePickerDialog(
                            onDismissRequest = { openEndDatePicker.value = false },
                            confirmButton = {
                                TextButton(onClick = {
                                    endDatePickerState.selectedDateMillis?.let { millis ->
                                        val date = Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate()
                                        selectedEnd = "%04d-%02d-%02d".format(date.year, date.monthValue, date.dayOfMonth)
                                    }
                                    openEndDatePicker.value = false
                                }) {
                                    Text("OK")
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = { openEndDatePicker.value = false }) {
                                    Text("Zrušit")
                                }
                            }
                        ) {
                            DatePicker(state = endDatePickerState)
                        }
                    }


                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        if (selectedSchool != "" && selectedType != "" && selectedStart != "") {
                            Button(
                                colors = ButtonDefaults.buttonColors(Color.Blue),
                                onClick = {
                                    var endDate: String? = null
                                    if (selectedEnd != "") {
                                        endDate = selectedEnd
                                    }
                                    val educationRequest = EducationRequest(
                                        id = existingEducation?.id,
                                        school = selectedSchool,
                                        type = selectedType,
                                        start = selectedStart,
                                        end = endDate,
                                        biography = null
                                    )
                                    onSubmit(educationRequest)
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
