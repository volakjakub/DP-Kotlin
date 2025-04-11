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
import org.adastra.curriculum.client.data.SkillRequest
import org.adastra.curriculum.client.data.SkillResponse
import org.adastra.curriculum.form.LanguageFormDialog
import org.adastra.curriculum.form.SkillFormDialog
import org.adastra.curriculum.service.BiographyService

@Composable
fun SkillList(biographyService: BiographyService, biography: BiographyResponse, account: AccountResponse) {
    var isLoadingSkills by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var showSkillForm by remember { mutableStateOf(false) }
    var skills by remember { mutableStateOf<List<SkillResponse>>(emptyList()) }
    var skillEdit by remember { mutableStateOf<SkillResponse?>(null) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        try {
            isLoadingSkills = true
            skills = biographyService.getSkillsByBiography(
                biography.id
            )
        } catch (e: Exception) {
            error = e.message
        } finally {
            isLoadingSkills = false
        }
    }

    fun createSkill(created: SkillResponse) {
        skills = skills + created
    }

    fun updateSkill(updated: SkillResponse) {
        skills = skills.map { skill ->
            if (skill.id == updated.id) updated else skill
        }
    }

    fun removeSkill(skill: SkillResponse) {
        skills = skills.filterNot { it.id == skill.id }
    }

    val onShowForm: (SkillResponse) -> Unit = { skill ->
        skillEdit = skill
        showSkillForm = true
    }

    val onDeleteSubmit: (SkillResponse) -> Unit = { skill ->
        coroutineScope.launch {
            try {
                isLoadingSkills = true
                if (biographyService.deleteSkill(skill.id)) {
                    removeSkill(skill)
                } else {
                    error = "Dovednost se nepodařilo odstranit."
                }
            } catch (e: Exception) {
                error = e.message
            } finally {
                isLoadingSkills = false
            }
        }
    }

    val onFormSubmit: (SkillRequest) -> Unit = { request ->
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
                isLoadingSkills = true
                if (request.id == null) {
                    createSkill(biographyService.createSkill(request))
                } else {
                    updateSkill(biographyService.updateSkill(request))
                }
            } catch (e: Exception) {
                error = e.message
            } finally {
                isLoadingSkills = false
            }
        }
    }

    Text(
        "Dovednosti",
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
            skillEdit = null
            showSkillForm = true
        }) {
        Text(
            "Vytvořit",
            color = Color.White,
        )
    }

    if (showSkillForm) {
        SkillFormDialog(
            isVisible = showSkillForm,
            onDismiss = { showSkillForm = false },
            onSubmit = onFormSubmit,
            existingSkill = skillEdit
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
    if (isLoadingSkills) {
        Column(modifier = Modifier.fillMaxWidth()) {
            CircularProgressIndicator(
                color = Color.Blue,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }

    when {
        skills.isNotEmpty() -> {
            skills.forEachIndexed { index, skill ->
                SkillBox(skill, onShowForm, onDeleteSubmit)
                if (index < skills.lastIndex) {
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