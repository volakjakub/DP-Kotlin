package org.adastra.curriculum.client.data

import kotlinx.serialization.Serializable

@Serializable
data class ProjectResponse (
    val id: Int,
    val name: String,
    val client: String,
    val start: String,
    val end: String?,
    val description: String?,
    val skills: List<ProjectSkillResponse>
) {
    fun getDesc(): String? {
        return description
    }
}