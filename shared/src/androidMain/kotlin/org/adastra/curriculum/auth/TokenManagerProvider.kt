package org.adastra.curriculum.auth

import android.content.Context
import com.russhwolf.settings.SharedPreferencesSettings

class TokenManagerProvider(private val context: Context) {

    fun createTokenManager(): TokenManager {
        val sharedPreferences = context.getSharedPreferences("curriculum_preferences", Context.MODE_PRIVATE)
        val settings = SharedPreferencesSettings(sharedPreferences)
        return TokenManager(settings)
    }
}