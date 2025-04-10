package org.adastra.curriculum.client.data

import kotlinx.serialization.Serializable

@Serializable
data class EducationRequest (
    val id: Int?,
    val school: String,
    val type: String,
    val start: String,
    val end: String?,
    var biography: BiographyRequest?
)