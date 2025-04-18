package org.adastra.curriculum.client.wrapper

import org.adastra.curriculum.client.data.LanguageRequest

class LanguageRequestWrapper {
    fun createLanguageRequest(
        name: String,
        expertise: Int
    ): LanguageRequest = LanguageRequest(
        null,
        name,
        expertise,
        null
    )

    fun updateLanguageRequest(
        id: Int,
        name: String,
        expertise: Int
    ): LanguageRequest = LanguageRequest(
        id,
        name,
        expertise,
        null
    )
}