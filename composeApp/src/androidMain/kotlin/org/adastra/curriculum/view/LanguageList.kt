package org.adastra.curriculum.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import kotlinx.coroutines.launch
import org.adastra.curriculum.client.data.AccountResponse
import org.adastra.curriculum.client.data.BiographyRequest
import org.adastra.curriculum.client.data.BiographyResponse
import org.adastra.curriculum.client.data.BiographyUserRequest
import org.adastra.curriculum.client.data.LanguageRequest
import org.adastra.curriculum.client.data.LanguageResponse
import org.adastra.curriculum.form.LanguageFormDialog
import org.adastra.curriculum.service.BiographyService

@Composable
fun LanguageList(biographyService: BiographyService, biography: BiographyResponse, account: AccountResponse) {
    var isLoadingLanguages by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var showLanguageForm by remember { mutableStateOf(false) }
    var languages by remember { mutableStateOf<List<LanguageResponse>>(emptyList()) }
    var languageEdit by remember { mutableStateOf<LanguageResponse?>(null) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        try {
            isLoadingLanguages = true
            languages = biographyService.getLanguagesByBiography(
                biography.id
            )
        } catch (e: Exception) {
            error = e.message
        } finally {
            isLoadingLanguages = false
        }
    }

    fun createLanguage(created: LanguageResponse) {
        languages = languages + created
    }

    fun updateLanguage(updated: LanguageResponse) {
        languages = languages.map { language ->
            if (language.id == updated.id) updated else language
        }
    }

    fun removeLanguage(language: LanguageResponse) {
        languages = languages.filterNot { it.id == language.id }
    }

    val onShowForm: (LanguageResponse) -> Unit = { language ->
        languageEdit = language
        showLanguageForm = true
    }

    val onDeleteSubmit: (LanguageResponse) -> Unit = { language ->
        coroutineScope.launch {
            try {
                isLoadingLanguages = true
                if (biographyService.deleteLanguage(language.id)) {
                    removeLanguage(language)
                } else {
                    error = "Jazyk se nepodařilo odstranit."
                }
            } catch (e: Exception) {
                error = e.message
            } finally {
                isLoadingLanguages = false
            }
        }
    }

    val onFormSubmit: (LanguageRequest) -> Unit = { request ->
        val biography = BiographyRequest(
            id = biography.id,
            title = biography.title ?: "",
            firstName = biography.firstName,
            lastName = biography.lastName,
            phone = biography.phone,
            email = biography.email,
            street = biography.street,
            city = biography.city,
            country = biography.country,
            position = biography.position,
            employedFrom = biography.employedFrom,
            user = BiographyUserRequest(
                id = account.id,
                login = account.login
            )
        )
        request.biography = biography
        coroutineScope.launch {
            try {
                isLoadingLanguages = true
                if (request.id == null) {
                    createLanguage(biographyService.createLanguage(request))
                } else {
                    updateLanguage(biographyService.updateLanguage(request))
                }
            } catch (e: Exception) {
                error = e.message
            } finally {
                isLoadingLanguages = false
            }
        }
    }

    Text(
        "Jazyky",
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
            languageEdit = null
            showLanguageForm = true
        }) {
        Text(
            "Vytvořit",
            color = Color.White,
        )
    }

    if (showLanguageForm) {
        LanguageFormDialog(
            isVisible = showLanguageForm,
            onDismiss = { showLanguageForm = false },
            onSubmit = onFormSubmit,
            existingLanguage = languageEdit,
            languageList = languages
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
    if (isLoadingLanguages) {
        Column(modifier = Modifier.fillMaxWidth()) {
            CircularProgressIndicator(
                color = Color.Blue,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }

    when {
        languages.isNotEmpty() -> {
            languages.forEachIndexed { index, language ->
                LanguageBox(language, onShowForm, onDeleteSubmit)
                if (index < languages.lastIndex) {
                    Divider(
                        color = Color.Gray,
                        thickness = 1.dp,
                        modifier = Modifier.padding(vertical = 5.dp)
                    )
                }
            }
        }
    }
}