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
import org.adastra.curriculum.client.data.ProjectRequest
import org.adastra.curriculum.client.data.ProjectResponse
import org.adastra.curriculum.client.data.SkillResponse
import org.adastra.curriculum.form.ProjectFormDialog
import org.adastra.curriculum.service.BiographyService

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProjectList(biographyService: BiographyService, biography: BiographyResponse, account: AccountResponse, skills: List<SkillResponse>, updateSkills: (List<SkillResponse>) -> Unit, canEdit: Boolean) {
    var isLoadingProjects by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var showProjectForm by remember { mutableStateOf(false) }
    var projects by remember { mutableStateOf<List<ProjectResponse>>(emptyList()) }
    var projectEdit by remember { mutableStateOf<ProjectResponse?>(null) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        try {
            isLoadingProjects = true
            projects = biographyService.getProjectsByBiography(
                biography.id
            )
        } catch (e: Exception) {
            error = e.message
        } finally {
            isLoadingProjects = false
        }
    }

    fun createProject(created: ProjectResponse) {
        projects = projects + created
    }

    fun updateProject(updated: ProjectResponse) {
        projects = projects.map { project ->
            if (project.id == updated.id) updated else project
        }
    }

    fun removeProject(project: ProjectResponse) {
        projects = projects.filterNot { it.id == project.id }
    }

    val onShowForm: (ProjectResponse) -> Unit = { project ->
        projectEdit = project
        showProjectForm = true
    }

    val onDeleteSubmit: (ProjectResponse) -> Unit = { project ->
        coroutineScope.launch {
            try {
                isLoadingProjects = true
                if (biographyService.deleteProject(project.id)) {
                    removeProject(project)
                } else {
                    error = "Vzdělání se nepodařilo odstranit."
                }
            } catch (e: Exception) {
                error = e.message
            } finally {
                isLoadingProjects = false
            }
        }
    }

    val onFormSubmit: (ProjectRequest) -> Unit = { request ->
        val bio = BiographyRequest(
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
        request.biography = bio
        coroutineScope.launch {
            try {
                isLoadingProjects = true
                if (request.id == null) {
                    createProject(biographyService.createProject(request))
                } else {
                    updateProject(biographyService.updateProject(request))
                }
            } catch (e: Exception) {
                error = e.message
            } finally {
                isLoadingProjects = false
            }

            try {
                updateSkills(biographyService.getSkillsByBiography(
                    biography.id
                ))
            } catch (e: Exception) {
                error = e.message
            }
        }
    }
    
    Text(
        "Projekty",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        style = MaterialTheme.typography.h5,
        textAlign = TextAlign.Left,
        color = Color.Black,
        modifier = Modifier.padding(bottom = 10.dp, top = 15.dp)
            .fillMaxWidth()
    )
    if (canEdit) {
        Button(
            colors = ButtonDefaults.buttonColors(Color.Blue),
            onClick = {
                projectEdit = null
                showProjectForm = true
            }) {
            Text(
                "Vytvořit",
                color = Color.White,
            )
        }
    }

    if (showProjectForm) {
        ProjectFormDialog(
            isVisible = showProjectForm,
            onDismiss = { showProjectForm = false },
            onSubmit = onFormSubmit,
            existingProject = projectEdit,
            skills = skills
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
    if (isLoadingProjects) {
        Column(modifier = Modifier.fillMaxWidth()) {
            CircularProgressIndicator(
                color = Color.Blue,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }

    when {
        projects.isNotEmpty() -> {
            projects.forEachIndexed { index, project ->
                AnimatedExpandableProjectBox(project, onShowForm, onDeleteSubmit, canEdit)
                if (index < projects.lastIndex) {
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