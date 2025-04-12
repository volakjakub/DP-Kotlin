package org.adastra.curriculum.client.data

import kotlinx.serialization.Serializable

@Serializable
data class AccountResponseCreate (
    val id: Int,
    val login: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val activated: Boolean,
    val langKey: String
)