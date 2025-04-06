package org.adastra.curriculum.view

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import org.adastra.curriculum.client.data.AccountResponse
import org.adastra.curriculum.client.data.BiographyRequest
import org.adastra.curriculum.client.data.BiographyResponse
import org.adastra.curriculum.form.BiographyForm
import org.adastra.curriculum.service.BiographyService

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BiographyDetail(biographyService: BiographyService, account: AccountResponse) {
    var showForm by remember { mutableStateOf(false) }
    var isLoadingBiography by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var biography by remember { mutableStateOf<BiographyResponse?>(null) }

    val coroutineScope = rememberCoroutineScope()

    // Load biography once on first composition
    LaunchedEffect(Unit) {
        try {
            isLoadingBiography = true
            biography = biographyService.getBiography()
        } catch (e: Exception) {
            error = e.message
        } finally {
            isLoadingBiography = false
        }
    }

    val onFormSubmit: (BiographyRequest) -> Unit = { request ->
        coroutineScope.launch {
            try {
                isLoadingBiography = true
                biography = if (request.id == 0) {
                    biographyService.createBiography(request)
                } else {
                    biographyService.updateBiography(request)
                }
                showForm = false
            } catch (e: Exception) {
                error = e.message
            } finally {
                isLoadingBiography = false
            }
        }
    }

    val onFormClose: () -> Unit = {
        showForm = false
    }

    Box(
        modifier = Modifier
            .padding()
            .background(Color.White)
            .animateContentSize(tween(durationMillis = 300)),
        contentAlignment = Alignment.TopStart
    ) {
        when {
            error != null -> {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = error!!,
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }

            isLoadingBiography -> {
                Column(modifier = Modifier.fillMaxWidth()) {
                    CircularProgressIndicator(
                        color = Color.Blue,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }

            showForm -> {
                BiographyForm(
                    biographyService = biographyService,
                    biography = biography,
                    account = account,
                    onFormSubmit = onFormSubmit,
                    onFormClose = onFormClose
                )
            }

            biography != null -> {
                val bio = biography!!
                Column(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
                    BiographyInfo(bio)
                    Button(
                        modifier = Modifier.fillMaxWidth().padding(),
                        colors = ButtonDefaults.buttonColors(Color.Blue),
                        onClick = { showForm = true }
                    ) {
                        Text("Upravit", color = Color.White)
                    }

                    LanguageList(biographyService, bio)
                    EducationList(biographyService, bio)
                    ProjectList(biographyService, bio)
                    SkillList(biographyService, bio)
                }
            }
        }
    }
}