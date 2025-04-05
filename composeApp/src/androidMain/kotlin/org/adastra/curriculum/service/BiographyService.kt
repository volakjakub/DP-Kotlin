package org.adastra.curriculum.service

import org.adastra.curriculum.BuildConfig
import org.adastra.curriculum.auth.LoginViewModel
import org.adastra.curriculum.client.BackendApi
import org.adastra.curriculum.client.data.BiographyResponse
import org.adastra.curriculum.client.data.EducationResponse
import org.adastra.curriculum.client.data.LanguageResponse

class BiographyService(loginViewModel: LoginViewModel) {
    private val baseUrl = BuildConfig.BASE_URL
    private val backendApi = BackendApi(baseUrl)
    private val lvm = loginViewModel
    private val token = lvm.getToken()
    private val username = lvm.getAccount()?.login

    suspend fun getBiography(): BiographyResponse {
        var biography: BiographyResponse? = null
        if (token == null || username == null) {
            lvm.logout()
            throw Exception("Chyba při načítání dat. Přihlaste se prosím znovu.")
        } else {
            try {
                biography = backendApi.getBiography(token, username)
            } catch (e: IllegalStateException) {
                lvm.logout()
                throw Exception(e.message)
            }
        }

        if (biography == null) {
            throw Exception("Chyba při načítání dat. Zkuste to prosím později.")
        } else {
            return biography
        }
    }

    suspend fun getLanguagesByBiography(biographyId: Int): List<LanguageResponse> {
        var languages: List<LanguageResponse>? = null
        if (token == null || username == null) {
            lvm.logout()
            throw Exception("Chyba při načítání dat. Přihlaste se prosím znovu.")
        } else {
            try {
                languages = backendApi.getLanguagesByBiography(token, biographyId)
            } catch (e: IllegalStateException) {
                lvm.logout()
                throw Exception(e.message)
            }
        }

        if (languages == null) {
            throw Exception("Chyba při načítání dat. Zkuste to prosím později.")
        } else {
            return languages
        }
    }

    suspend fun getEducationsByBiography(biographyId: Int): List<EducationResponse> {
        var educations: List<EducationResponse>? = null
        if (token == null || username == null) {
            lvm.logout()
            throw Exception("Chyba při načítání dat. Přihlaste se prosím znovu.")
        } else {
            try {
                educations = backendApi.getEducationsByBiography(token, biographyId)
            } catch (e: IllegalStateException) {
                lvm.logout()
                throw Exception(e.message)
            }
        }

        if (educations == null) {
            throw Exception("Chyba při načítání dat. Zkuste to prosím později.")
        } else {
            return educations
        }
    }
}