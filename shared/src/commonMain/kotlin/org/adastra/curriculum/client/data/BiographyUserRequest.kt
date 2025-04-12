package org.adastra.curriculum.client.data

import kotlinx.serialization.Serializable

@Serializable
data class BiographyUserRequest (
    val id: Int,
    val login: String
)