package org.adastra.curriculum.auth

import com.russhwolf.settings.Settings

class TokenManager(val settings: Settings) {
    fun saveToken(token: String) {
        settings.putString("auth_token", token)
    }

    fun getToken(): String? {
        return settings.getStringOrNull("auth_token")
    }

    fun clearToken() {
        settings.remove("auth_token")
    }
}