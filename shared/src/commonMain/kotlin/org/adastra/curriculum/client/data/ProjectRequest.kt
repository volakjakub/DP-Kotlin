package org.adastra.curriculum.client.data

import kotlinx.serialization.Serializable

@Serializable
data class ProjectRequest (
    val id: Int?,
    val name: String,
    val client: String,
    val start: String,
    val end: String?,
    val description: String?,
    var skills: List<ProjectSkillRequest>,
    var biography: BiographyRequest?
)