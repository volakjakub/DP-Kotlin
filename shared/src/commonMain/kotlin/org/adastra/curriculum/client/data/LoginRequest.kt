package org.adastra.curriculum.client.data

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(val username: String, val password: String, val rememberMe: Boolean)