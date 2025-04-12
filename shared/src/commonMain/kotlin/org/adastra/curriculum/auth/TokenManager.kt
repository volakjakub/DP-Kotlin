package org.adastra.curriculum.auth

import com.russhwolf.settings.Settings
import kotlinx.serialization.json.Json
import org.adastra.curriculum.client.data.AccountResponse

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

    fun saveAccount(account: AccountResponse) {
        settings.putString("account", Json.encodeToString(AccountResponse.serializer(), account))
    }

    fun getAccount(): AccountResponse? {
        val jsonString = settings.getStringOrNull("account")
        if (jsonString != null) {
            val account: AccountResponse = Json.decodeFromString(jsonString)
            return account
        } else {
            return null
        }
    }

    fun clearAccount() {
        settings.remove("account")
    }
}