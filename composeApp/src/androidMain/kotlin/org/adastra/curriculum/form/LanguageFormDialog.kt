package org.adastra.curriculum.form

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import org.adastra.curriculum.client.data.LanguageRequest
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import org.adastra.curriculum.client.data.Language
import org.adastra.curriculum.client.data.LanguageResponse
import org.adastra.curriculum.helper.ExpertiseHelper
import org.adastra.curriculum.helper.LanguageHelper

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LanguageFormDialog(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onSubmit: (LanguageRequest) -> Unit,
    existingLanguage: LanguageResponse? = null,
    languageList: List<LanguageResponse>
) {
    val languageHelper = LanguageHelper()
    val expertiseHelper = ExpertiseHelper()
    if (isVisible) {
        Dialog(onDismissRequest = onDismiss) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colors.background
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    var selectedLanguage by remember { mutableStateOf(existingLanguage?.name ?: "") }
                    var expertise by remember { mutableStateOf(existingLanguage?.expertise ?: 1) }

                    // Language Dropdown
                    var expandedLanguage by remember { mutableStateOf(false) }
                    val languageOptions = listOf(Language.CZECH, Language.ENGLISH, Language.SLOVAK)

                    ExposedDropdownMenuBox(
                        expanded = expandedLanguage,
                        onExpandedChange = { expandedLanguage = !expandedLanguage },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = languageHelper.getLanguage(selectedLanguage),
                            onValueChange = {},
                            label = { Text("Název") },
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
                            expanded = expandedLanguage,
                            onDismissRequest = { expandedLanguage = false }
                        ) {
                            languageOptions.forEach { language ->
                                if (languageList.none { it.name == language.displayName }) {
                                    DropdownMenuItem(onClick = {
                                        selectedLanguage = language.displayName
                                        expandedLanguage = false
                                    }) {
                                        Text(text = languageHelper.getLanguage(language.displayName))
                                    }
                                }
                            }
                        }
                    }

                    // Expertise Dropdown (1 to 5)
                    var expandedExpertise by remember { mutableStateOf(false) }
                    val expertiseOptions = (1..5).toList()
                    ExposedDropdownMenuBox(
                        expanded = expandedExpertise,
                        onExpandedChange = { expandedExpertise = !expandedExpertise },
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                    ) {
                        OutlinedTextField(
                            value = expertiseHelper.getExpertise(expertise),
                            onValueChange = {},
                            label = { Text("Zkušenost") },
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
                            expanded = expandedExpertise,
                            onDismissRequest = { expandedExpertise = false }
                        ) {
                            expertiseOptions.forEach { level ->
                                DropdownMenuItem(onClick = {
                                    expertise = level
                                    expandedExpertise = false
                                }) {
                                    Text(text = expertiseHelper.getExpertise(level))
                                }
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        if (selectedLanguage != "") {
                            Button(
                                colors = ButtonDefaults.buttonColors(Color.Blue),
                                onClick = {
                                    val languageRequest = LanguageRequest(
                                        id = existingLanguage?.id,
                                        name = selectedLanguage,
                                        expertise = expertise,
                                        biography = null
                                    )
                                    onSubmit(languageRequest)
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
