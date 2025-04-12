package org.adastra.curriculum.client.data

import kotlinx.serialization.Serializable

@Serializable
data class ProjectSkillResponse (
    val id: Int,
    val name: String
)