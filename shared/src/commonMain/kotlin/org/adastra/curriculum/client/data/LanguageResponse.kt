package org.adastra.curriculum.client.data

import kotlinx.serialization.Serializable

@Serializable
data class LanguageResponse (
    val id: Int,
    val name: String,
    val expertise: Int
)