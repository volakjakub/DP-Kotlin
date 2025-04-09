package org.adastra.curriculum.client.wrapper

import org.adastra.curriculum.client.data.BiographyRequest
import org.adastra.curriculum.client.data.LanguageRequest

class LanguageRequestWrapper {
    fun createLanguageRequest(
        name: String,
        expertise: Int,
        biography: BiographyRequest
    ): LanguageRequest = LanguageRequest(
        null,
        name,
        expertise,
        biography
    )

    fun updateLanguageRequest(
        id: Int,
        name: String,
        expertise: Int,
        biography: BiographyRequest
    ): LanguageRequest = LanguageRequest(
        id,
        name,
        expertise,
        biography
    )
}