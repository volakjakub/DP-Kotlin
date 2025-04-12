package org.adastra.curriculum.client.data

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(val id_token: String)