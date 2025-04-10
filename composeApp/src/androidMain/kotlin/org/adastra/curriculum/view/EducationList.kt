package org.adastra.curriculum.view

import android.os.Build
import androidx.annotation.RequiresApi
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
import org.adastra.curriculum.client.data.EducationRequest
import org.adastra.curriculum.client.data.EducationResponse
import org.adastra.curriculum.form.EducationFormDialog
import org.adastra.curriculum.service.BiographyService

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EducationList(biographyService: BiographyService, biography: BiographyResponse, account: AccountResponse) {
    var isLoadingEducations by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var showEducationForm by remember { mutableStateOf(false) }
    var educations by remember { mutableStateOf<List<EducationResponse>>(emptyList()) }
    var educationEdit by remember { mutableStateOf<EducationResponse?>(null) }
    val coroutineScope = rememberCoroutineScope()

    // Load biography once on first composition
    LaunchedEffect(Unit) {
        try {
            isLoadingEducations = true
            educations = biographyService.getEducationsByBiography(
                biography.id
            )
        } catch (e: Exception) {
            error = e.message
        } finally {
            isLoadingEducations = false
        }
    }

    fun createEducation(created: EducationResponse) {
        educations = educations + created
    }

    fun updateEducation(updated: EducationResponse) {
        educations = educations.map { education ->
            if (education.id == updated.id) updated else education
        }
    }

    fun removeEducation(education: EducationResponse) {
        educations = educations.filterNot { it.id == education.id }
    }

    val onShowForm: (EducationResponse) -> Unit = { education ->
        educationEdit = education
        showEducationForm = true
    }

    val onDeleteSubmit: (EducationResponse) -> Unit = { education ->
        coroutineScope.launch {
            try {
                isLoadingEducations = true
                if (biographyService.deleteEducation(education.id)) {
                    removeEducation(education)
                } else {
                    error = "Vzdělání se nepodařilo odstranit."
                }
            } catch (e: Exception) {
                error = e.message
            } finally {
                isLoadingEducations = false
            }
        }
    }

    val onFormSubmit: (EducationRequest) -> Unit = { request ->
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
                isLoadingEducations = true
                if (request.id == null) {
                    createEducation(biographyService.createEducation(request))
                } else {
                    updateEducation(biographyService.updateEducation(request))
                }
            } catch (e: Exception) {
                error = e.message
            } finally {
                isLoadingEducations = false
            }
        }
    }

    Text(
        "Vzdělání",
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
            educationEdit = null
            showEducationForm = true
        }) {
        Text(
            "Vytvořit",
            color = Color.White,
        )
    }

    if (showEducationForm) {
        EducationFormDialog(
            isVisible = showEducationForm,
            onDismiss = { showEducationForm = false },
            onSubmit = onFormSubmit,
            existingEducation = educationEdit
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
    if (isLoadingEducations) {
        Column(modifier = Modifier.fillMaxWidth()) {
            CircularProgressIndicator(
                color = Color.Blue,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }



    when {
        educations.isNotEmpty() -> {
            educations.forEachIndexed { index, education ->
                EducationBox(education, onShowForm, onDeleteSubmit)
                if (index < educations.lastIndex) {
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