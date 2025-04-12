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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Checkbox
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
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
import kotlinx.datetime.LocalDate
import org.adastra.curriculum.client.data.ProjectRequest
import org.adastra.curriculum.client.data.ProjectResponse
import org.adastra.curriculum.client.data.ProjectSkillRequest
import org.adastra.curriculum.client.data.SkillResponse
import java.time.Instant
import java.time.ZoneId

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ProjectFormDialog(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onSubmit: (ProjectRequest) -> Unit,
    existingProject: ProjectResponse? = null,
    skills: List<SkillResponse>
) {
    val scrollState = rememberScrollState()
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
                Column(modifier = Modifier.verticalScroll(scrollState).padding(16.dp)) {
                    var selectedName by remember { mutableStateOf(existingProject?.name ?: "") }
                    var selectedClient by remember { mutableStateOf(existingProject?.client ?: "") }
                    var selectedDescription by remember { mutableStateOf(existingProject?.description ?: "") }
                    var selectedStart by remember { mutableStateOf(existingProject?.start ?: "") }
                    var selectedEnd by remember { mutableStateOf(existingProject?.end ?: "") }
                    val selectedSkills = remember {
                        mutableStateMapOf<Int, Boolean>().apply {
                            skills.forEach { skill ->
                                this[skill.id] = existingProject?.skills?.any { it.id == skill.id } == true
                            }
                        }
                    }

                    OutlinedTextField(modifier = Modifier.fillMaxWidth(), value = selectedName, onValueChange = { selectedName = it }, label = { Text("Název projektu:") })
                    OutlinedTextField(modifier = Modifier.fillMaxWidth(), value = selectedClient, onValueChange = { selectedClient = it }, label = { Text("Klient:") })
                    OutlinedTextField(modifier = Modifier.fillMaxWidth(), value = selectedDescription, onValueChange = { selectedDescription = it }, label = { Text("Popis projektu:") })

                    if (selectedStart != "") {
                        val startDate = LocalDate.parse(selectedStart)
                        val startDateFormatted = "%02d.%02d.%04d".format(
                            startDate.dayOfMonth,
                            startDate.monthNumber,
                            startDate.year
                        )
                        Text(
                            text = "Počátek projektu: $startDateFormatted",
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                        )
                    } else {
                        Text(
                            text = "Počátek projektu: -",
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
                            text = "Ukončení projektu: $endDateFormatted",
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                        )
                    } else {
                        Text(
                            text = "Ukončení projektu: -",
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

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Dovednosti:",
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                    )
                    skills.forEach { skill ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            Checkbox(
                                checked = selectedSkills[skill.id] == true,
                                onCheckedChange = { checked ->
                                    selectedSkills[skill.id] = checked
                                }
                            )
                            Text(skill.name, modifier = Modifier.padding(start = 8.dp))
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        if (selectedName != "" && selectedClient != "" && selectedStart != "") {
                            Button(
                                colors = ButtonDefaults.buttonColors(Color.Blue),
                                onClick = {
                                    var endDate: String? = null
                                    if (selectedEnd != "") {
                                        endDate = selectedEnd
                                    }
                                    val projectRequest = ProjectRequest(
                                        id = existingProject?.id,
                                        name = selectedName,
                                        client = selectedClient,
                                        start = selectedStart,
                                        end = endDate,
                                        description = selectedDescription,
                                        skills = skills.filter { selectedSkills[it.id] == true }.map { skill -> ProjectSkillRequest(id = skill.id) },
                                        biography = null
                                    )
                                    onSubmit(projectRequest)
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
