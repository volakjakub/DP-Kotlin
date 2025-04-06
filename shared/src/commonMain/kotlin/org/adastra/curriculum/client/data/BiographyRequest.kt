package org.adastra.curriculum.client.data

import kotlinx.serialization.Serializable

@Serializable
data class BiographyRequest (
    val id: Int?,
    val title: String,
    val firstName: String,
    val lastName: String,
    val phone: String,
    val email: String,
    val street: String,
    val city: String,
    val country: String,
    val position: String,
    val employedFrom: String,
    val user: BiographyUserRequest
)