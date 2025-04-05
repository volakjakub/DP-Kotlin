package org.adastra.curriculum.client.data

import kotlinx.serialization.Serializable

@Serializable
data class AccountResponse (
    val id: Int,
    val login: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val activated: Boolean,
    val authorities: List<Authority>
)