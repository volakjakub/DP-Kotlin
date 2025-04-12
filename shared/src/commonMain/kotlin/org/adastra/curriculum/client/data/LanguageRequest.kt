package org.adastra.curriculum.client.data

import kotlinx.serialization.Serializable

@Serializable
data class LanguageRequest (
    val id: Int?,
    val name: String,
    val expertise: Int,
    var biography: BiographyRequest?
)