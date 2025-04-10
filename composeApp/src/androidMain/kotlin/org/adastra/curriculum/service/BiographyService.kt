package org.adastra.curriculum.service

import org.adastra.curriculum.BuildConfig
import org.adastra.curriculum.auth.LoginViewModel
import org.adastra.curriculum.client.BackendApi
import org.adastra.curriculum.client.data.BiographyRequest
import org.adastra.curriculum.client.data.BiographyResponse
import org.adastra.curriculum.client.data.EducationRequest
import org.adastra.curriculum.client.data.EducationResponse
import org.adastra.curriculum.client.data.LanguageRequest
import org.adastra.curriculum.client.data.LanguageResponse
import org.adastra.curriculum.client.data.ProjectResponse
import org.adastra.curriculum.client.data.SkillResponse
import org.adastra.curriculum.exception.NotFoundException

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
            } catch (_: Exception) {
                throw Exception("Chyba při načítání dat. Zkuste to prosím později.")
            }
        }

        if (biography == null) {
            throw NotFoundException()
        } else {
            return biography
        }
    }

    suspend fun createBiography(biographyRequest: BiographyRequest): BiographyResponse {
        var biography: BiographyResponse? = null
        if (token == null || username == null) {
            lvm.logout()
            throw Exception("Chyba při načítání dat. Přihlaste se prosím znovu.")
        } else {
            try {
                biography = backendApi.saveBiography(token, biographyRequest, null)
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

    suspend fun updateBiography(biographyRequest: BiographyRequest): BiographyResponse {
        var biography: BiographyResponse? = null
        if (token == null || username == null) {
            lvm.logout()
            throw Exception("Chyba při načítání dat. Přihlaste se prosím znovu.")
        } else {
            try {
                biography = backendApi.saveBiography(token, biographyRequest, biographyRequest.id)
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

    suspend fun createLanguage(languageRequest: LanguageRequest): LanguageResponse {
        var language: LanguageResponse? = null
        if (token == null || username == null) {
            lvm.logout()
            throw Exception("Chyba při načítání dat. Přihlaste se prosím znovu.")
        } else {
            try {
                language = backendApi.saveLanguage(token, languageRequest, null)
            } catch (e: IllegalStateException) {
                lvm.logout()
                throw Exception(e.message)
            }
        }

        if (language == null) {
            throw Exception("Chyba při ukládání dat. Zkontrolujte prosím zadané údaje.")
        } else {
            return language
        }
    }

    suspend fun updateLanguage(languageRequest: LanguageRequest): LanguageResponse {
        var language: LanguageResponse? = null
        if (token == null || username == null) {
            lvm.logout()
            throw Exception("Chyba při načítání dat. Přihlaste se prosím znovu.")
        } else {
            try {
                language = backendApi.saveLanguage(token, languageRequest, languageRequest.id)
            } catch (e: IllegalStateException) {
                lvm.logout()
                throw Exception(e.message)
            }
        }

        if (language == null) {
            throw Exception("Chyba při ukládání dat. Zkontrolujte prosím zadané údaje.")
        } else {
            return language
        }
    }

    suspend fun deleteLanguage(languageId: Int): Boolean {
        if (token == null || username == null) {
            lvm.logout()
            throw Exception("Chyba při načítání dat. Přihlaste se prosím znovu.")
        } else {
            try {
                return backendApi.deleteLanguage(token, languageId)
            } catch (e: IllegalStateException) {
                lvm.logout()
                throw Exception(e.message)
            }
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

    suspend fun createEducation(educationRequest: EducationRequest): EducationResponse {
        var education: EducationResponse? = null
        if (token == null || username == null) {
            lvm.logout()
            throw Exception("Chyba při načítání dat. Přihlaste se prosím znovu.")
        } else {
            try {
                education = backendApi.saveEducation(token, educationRequest, null)
            } catch (e: IllegalStateException) {
                lvm.logout()
                throw Exception(e.message)
            }
        }

        if (education == null) {
            throw Exception("Chyba při ukládání dat. Zkontrolujte prosím zadané údaje.")
        } else {
            return education
        }
    }

    suspend fun updateEducation(educationRequest: EducationRequest): EducationResponse {
        var education: EducationResponse? = null
        if (token == null || username == null) {
            lvm.logout()
            throw Exception("Chyba při načítání dat. Přihlaste se prosím znovu.")
        } else {
            try {
                education = backendApi.saveEducation(token, educationRequest, educationRequest.id)
            } catch (e: IllegalStateException) {
                lvm.logout()
                throw Exception(e.message)
            }
        }

        if (education == null) {
            throw Exception("Chyba při ukládání dat. Zkontrolujte prosím zadané údaje.")
        } else {
            return education
        }
    }

    suspend fun deleteEducation(educationId: Int): Boolean {
        if (token == null || username == null) {
            lvm.logout()
            throw Exception("Chyba při načítání dat. Přihlaste se prosím znovu.")
        } else {
            try {
                return backendApi.deleteEducation(token, educationId)
            } catch (e: IllegalStateException) {
                lvm.logout()
                throw Exception(e.message)
            }
        }
    }

    suspend fun getProjectsByBiography(biographyId: Int): List<ProjectResponse> {
        var projects: List<ProjectResponse>? = null
        if (token == null || username == null) {
            lvm.logout()
            throw Exception("Chyba při načítání dat. Přihlaste se prosím znovu.")
        } else {
            try {
                projects = backendApi.getProjectsByBiography(token, biographyId)
            } catch (e: IllegalStateException) {
                lvm.logout()
                throw Exception(e.message)
            }
        }

        if (projects == null) {
            throw Exception("Chyba při načítání dat. Zkuste to prosím později.")
        } else {
            return projects
        }
    }

    suspend fun getSkillsByBiography(biographyId: Int): List<SkillResponse> {
        var skills: List<SkillResponse>? = null
        if (token == null || username == null) {
            lvm.logout()
            throw Exception("Chyba při načítání dat. Přihlaste se prosím znovu.")
        } else {
            try {
                skills = backendApi.getSkillsByBiography(token, biographyId)
            } catch (e: IllegalStateException) {
                lvm.logout()
                throw Exception(e.message)
            }
        }

        if (skills == null) {
            throw Exception("Chyba při načítání dat. Zkuste to prosím později.")
        } else {
            return skills
        }
    }
}