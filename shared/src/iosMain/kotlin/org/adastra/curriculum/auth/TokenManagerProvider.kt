package org.adastra.curriculum.auth

import com.russhwolf.settings.NSUserDefaultsSettings
import platform.Foundation.NSUserDefaults

class TokenManagerProvider {
    fun createTokenManager(): TokenManager {
        val userDefaults = NSUserDefaults.standardUserDefaults
        val settings = NSUserDefaultsSettings(userDefaults)  // Use NSUserDefaultsSettings on iOS
        return TokenManager(settings)
    }
}