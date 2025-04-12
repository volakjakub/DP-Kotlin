package org.adastra.curriculum.client

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.accept
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.adastra.curriculum.client.data.AccountRequest
import org.adastra.curriculum.client.data.AccountResponse
import org.adastra.curriculum.client.data.BiographyRequest
import org.adastra.curriculum.client.data.BiographyResponse
import org.adastra.curriculum.client.data.EducationRequest
import org.adastra.curriculum.client.data.EducationResponse
import org.adastra.curriculum.client.data.LanguageRequest
import org.adastra.curriculum.client.data.LanguageResponse
import org.adastra.curriculum.client.data.LoginRequest
import org.adastra.curriculum.client.data.LoginResponse
import org.adastra.curriculum.client.data.ProjectRequest
import org.adastra.curriculum.client.data.ProjectResponse
import org.adastra.curriculum.client.data.SkillRequest
import org.adastra.curriculum.client.data.SkillResponse

class BackendApi(val baseUrl: String) {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
            })
        }

        install(Logging) {
            logger = Logger.SIMPLE
            level = LogLevel.BODY
        }

        defaultRequest {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
        }
    }

    suspend fun getSkillsByBiography(token: String, biographyId: Int): List<SkillResponse>? {
        val response: HttpResponse = client.get("$baseUrl/skills/biography?biographyId=$biographyId") {
            header(HttpHeaders.Authorization, "Bearer $token")
        }

        if (response.status == HttpStatusCode.Unauthorized) {
            println("Invalid credentials (401). Please log in.")
            throw IllegalStateException("Invalid credentials (401). Please log in.")
        }

        if (response.status == HttpStatusCode.OK) {
            val skills: List<SkillResponse> = response.body()
            return skills
        } else {
            println("Invalid request. Please try again.")
            return null
        }
    }

    suspend fun getProjectsByBiography(token: String, biographyId: Int): List<ProjectResponse>? {
        val response: HttpResponse = client.get("$baseUrl/projects/biography?biographyId=$biographyId") {
            header(HttpHeaders.Authorization, "Bearer $token")
        }

        if (response.status == HttpStatusCode.Unauthorized) {
            println("Invalid credentials (401). Please log in.")
            throw IllegalStateException("Invalid credentials (401). Please log in.")
        }

        if (response.status == HttpStatusCode.OK) {
            val projects: List<ProjectResponse> = response.body()
            return projects
        } else {
            println("Invalid request. Please try again.")
            return null
        }
    }

    suspend fun getEducationsByBiography(token: String, biographyId: Int): List<EducationResponse>? {
        val response: HttpResponse = client.get("$baseUrl/educations/biography?biographyId=$biographyId") {
            header(HttpHeaders.Authorization, "Bearer $token")
        }

        if (response.status == HttpStatusCode.Unauthorized) {
            println("Invalid credentials (401). Please log in.")
            throw IllegalStateException("Invalid credentials (401). Please log in.")
        }

        if (response.status == HttpStatusCode.OK) {
            val educations: List<EducationResponse> = response.body()
            return educations
        } else {
            println("Invalid request. Please try again.")
            return null
        }
    }

    suspend fun getLanguagesByBiography(token: String, biographyId: Int): List<LanguageResponse>? {
        val response: HttpResponse = client.get("$baseUrl/languages/biography?biographyId=$biographyId") {
            header(HttpHeaders.Authorization, "Bearer $token")
        }

        if (response.status == HttpStatusCode.Unauthorized) {
            println("Invalid credentials (401). Please log in.")
            throw IllegalStateException("Invalid credentials (401). Please log in.")
        }

        if (response.status == HttpStatusCode.OK) {
            val languages: List<LanguageResponse> = response.body()
            return languages
        } else {
            println("Invalid request. Please try again.")
            return null
        }
    }

    suspend fun saveLanguage(token: String, languageRequest: LanguageRequest, languageId: Int?): LanguageResponse? {
        var response: HttpResponse
        if (languageId == null) {
            response = client.post("$baseUrl/languages") {
                header(HttpHeaders.Authorization, "Bearer $token")
                setBody(languageRequest)
            }
        } else {
            response = client.put("$baseUrl/languages/$languageId") {
                header(HttpHeaders.Authorization, "Bearer $token")
                setBody(languageRequest)
            }
        }

        if (response.status == HttpStatusCode.Unauthorized) {
            println("Invalid credentials (401). Please log in.")
            throw IllegalStateException("Invalid credentials (401). Please log in.")
        }

        if (response.status == HttpStatusCode.OK || response.status == HttpStatusCode.Created) {
            val language: LanguageResponse = response.body()
            return language
        } else {
            println("Invalid request. Please try again.")
            return null
        }
    }

    suspend fun deleteLanguage(token: String, languageId: Int): Boolean {
        var response: HttpResponse = client.delete("$baseUrl/languages/$languageId") {
            header(HttpHeaders.Authorization, "Bearer $token")
        }

        if (response.status == HttpStatusCode.Unauthorized) {
            println("Invalid credentials (401). Please log in.")
            throw IllegalStateException("Invalid credentials (401). Please log in.")
        }

        if (response.status == HttpStatusCode.OK || response.status == HttpStatusCode.NoContent) {
            return true
        } else {
            println("Invalid request. Please try again.")
            return false
        }
    }

    suspend fun saveEducation(token: String, educationRequest: EducationRequest, educationId: Int?): EducationResponse? {
        var response: HttpResponse
        if (educationId == null) {
            response = client.post("$baseUrl/educations") {
                header(HttpHeaders.Authorization, "Bearer $token")
                setBody(educationRequest)
            }
        } else {
            response = client.put("$baseUrl/educations/$educationId") {
                header(HttpHeaders.Authorization, "Bearer $token")
                setBody(educationRequest)
            }
        }

        if (response.status == HttpStatusCode.Unauthorized) {
            println("Invalid credentials (401). Please log in.")
            throw IllegalStateException("Invalid credentials (401). Please log in.")
        }

        if (response.status == HttpStatusCode.OK || response.status == HttpStatusCode.Created) {
            val education: EducationResponse = response.body()
            return education
        } else {
            println("Invalid request. Please try again.")
            return null
        }
    }

    suspend fun deleteEducation(token: String, educationId: Int): Boolean {
        var response: HttpResponse = client.delete("$baseUrl/educations/$educationId") {
            header(HttpHeaders.Authorization, "Bearer $token")
        }

        if (response.status == HttpStatusCode.Unauthorized) {
            println("Invalid credentials (401). Please log in.")
            throw IllegalStateException("Invalid credentials (401). Please log in.")
        }

        if (response.status == HttpStatusCode.OK || response.status == HttpStatusCode.NoContent) {
            return true
        } else {
            println("Invalid request. Please try again.")
            return false
        }
    }

    suspend fun saveSkill(token: String, skillRequest: SkillRequest, skillId: Int?): SkillResponse? {
        var response: HttpResponse
        if (skillId == null) {
            response = client.post("$baseUrl/skills") {
                header(HttpHeaders.Authorization, "Bearer $token")
                setBody(skillRequest)
            }
        } else {
            response = client.put("$baseUrl/skills/$skillId") {
                header(HttpHeaders.Authorization, "Bearer $token")
                setBody(skillRequest)
            }
        }

        if (response.status == HttpStatusCode.Unauthorized) {
            println("Invalid credentials (401). Please log in.")
            throw IllegalStateException("Invalid credentials (401). Please log in.")
        }

        if (response.status == HttpStatusCode.OK || response.status == HttpStatusCode.Created) {
            val skill: SkillResponse = response.body()
            return skill
        } else {
            println("Invalid request. Please try again.")
            return null
        }
    }

    suspend fun deleteSkill(token: String, skillId: Int): Boolean {
        var response: HttpResponse = client.delete("$baseUrl/skills/$skillId") {
            header(HttpHeaders.Authorization, "Bearer $token")
        }

        if (response.status == HttpStatusCode.Unauthorized) {
            println("Invalid credentials (401). Please log in.")
            throw IllegalStateException("Invalid credentials (401). Please log in.")
        }

        if (response.status == HttpStatusCode.OK || response.status == HttpStatusCode.NoContent) {
            return true
        } else {
            println("Invalid request. Please try again.")
            return false
        }
    }

    suspend fun saveProject(token: String, projectRequest: ProjectRequest, projectId: Int?): ProjectResponse? {
        var response: HttpResponse
        if (projectId == null) {
            response = client.post("$baseUrl/projects") {
                header(HttpHeaders.Authorization, "Bearer $token")
                setBody(projectRequest)
            }
        } else {
            response = client.put("$baseUrl/projects/$projectId") {
                header(HttpHeaders.Authorization, "Bearer $token")
                setBody(projectRequest)
            }
        }

        if (response.status == HttpStatusCode.Unauthorized) {
            println("Invalid credentials (401). Please log in.")
            throw IllegalStateException("Invalid credentials (401). Please log in.")
        }

        if (response.status == HttpStatusCode.OK || response.status == HttpStatusCode.Created) {
            val project: ProjectResponse = response.body()
            return project
        } else {
            println("Invalid request. Please try again.")
            return null
        }
    }

    suspend fun deleteProject(token: String, projectId: Int): Boolean {
        var response: HttpResponse = client.delete("$baseUrl/projects/$projectId") {
            header(HttpHeaders.Authorization, "Bearer $token")
        }

        if (response.status == HttpStatusCode.Unauthorized) {
            println("Invalid credentials (401). Please log in.")
            throw IllegalStateException("Invalid credentials (401). Please log in.")
        }

        if (response.status == HttpStatusCode.OK || response.status == HttpStatusCode.NoContent) {
            return true
        } else {
            println("Invalid request. Please try again.")
            return false
        }
    }

    suspend fun getBiography(token: String, username: String): BiographyResponse? {
        val response: HttpResponse = client.get("$baseUrl/biographies/user?username=$username") {
            header(HttpHeaders.Authorization, "Bearer $token")
        }

        if (response.status == HttpStatusCode.Unauthorized) {
            println("Invalid credentials (401). Please log in.")
            throw IllegalStateException("Invalid credentials (401). Please log in.")
        }

        if (response.status == HttpStatusCode.NotFound) {
            println("Biography not found. Please create one.")
            return null
        }

        if (response.status == HttpStatusCode.OK) {
            val biography: BiographyResponse = response.body()
            return biography
        } else {
            println("Invalid request. Please try again.")
            throw Exception("Invalid request. Please try again.")
        }
    }

    suspend fun saveBiography(token: String, biographyRequest: BiographyRequest, biographyId: Int?): BiographyResponse? {
        var response: HttpResponse
        if (biographyId == null) {
            response = client.post("$baseUrl/biographies") {
                header(HttpHeaders.Authorization, "Bearer $token")
                setBody(biographyRequest)
            }
        } else {
            response = client.put("$baseUrl/biographies/$biographyId") {
                header(HttpHeaders.Authorization, "Bearer $token")
                setBody(biographyRequest)
            }
        }

        if (response.status == HttpStatusCode.Unauthorized) {
            println("Invalid credentials (401). Please log in.")
            throw IllegalStateException("Invalid credentials (401). Please log in.")
        }

        if (response.status == HttpStatusCode.OK || response.status == HttpStatusCode.Created) {
            val biography: BiographyResponse = response.body()
            return biography
        } else {
            println("Invalid request. Please try again.")
            return null
        }
    }

    suspend fun getAccount(token: String): AccountResponse? {
        return try {
            val response: HttpResponse = client.get("$baseUrl/account") {
                header(HttpHeaders.Authorization, "Bearer $token")
            }

            if (response.status == HttpStatusCode.Unauthorized) {
                println("Invalid credentials (401). Please log in.")
                return null
            }

            if (response.status == HttpStatusCode.OK) {
                val account: AccountResponse = response.body()
                return account
            } else {
                println("Invalid request. Please try again.")
                return null
            }
        } catch (e: Exception) {
            println("Invalid request: ${e.message}")
            return null
        }
    }

    suspend fun getUsers(token: String, page: Int = 0, size: Int = 10): List<AccountResponse> {
        return try {
            val response: HttpResponse = client.get("$baseUrl/admin/users?page=$page&size=$size") {
                header(HttpHeaders.Authorization, "Bearer $token")
            }

            if (response.status == HttpStatusCode.Unauthorized) {
                println("Invalid credentials (401). Please log in.")
                return emptyList()
            }

            if (response.status == HttpStatusCode.OK) {
                val users: List<AccountResponse> = response.body()
                return users
            } else {
                println("Invalid request. Please try again.")
                return emptyList()
            }
        } catch (e: Exception) {
            println("Invalid request: ${e.message}")
            return emptyList()
        }
    }

    suspend fun saveUser(token: String, accountRequest: AccountRequest, accountId: Int?): AccountResponse? {
        var response: HttpResponse
        if (accountId == null) {
            response = client.post("$baseUrl/admin/users") {
                header(HttpHeaders.Authorization, "Bearer $token")
                setBody(accountRequest)
            }
        } else {
            response = client.put("$baseUrl/admin/users/$accountId") {
                header(HttpHeaders.Authorization, "Bearer $token")
                setBody(accountRequest)
            }
        }

        if (response.status == HttpStatusCode.Unauthorized) {
            println("Invalid credentials (401). Please log in.")
            throw IllegalStateException("Invalid credentials (401). Please log in.")
        }

        if (response.status == HttpStatusCode.OK || response.status == HttpStatusCode.Created) {
            val user: AccountResponse = response.body()
            return user
        } else {
            println("Invalid request. Please try again.")
            return null
        }
    }

    suspend fun deleteUser(token: String, accountId: Int): Boolean {
        var response: HttpResponse = client.delete("$baseUrl/api/users/$accountId") {
            header(HttpHeaders.Authorization, "Bearer $token")
        }

        if (response.status == HttpStatusCode.Unauthorized) {
            println("Invalid credentials (401). Please log in.")
            throw IllegalStateException("Invalid credentials (401). Please log in.")
        }

        if (response.status == HttpStatusCode.OK || response.status == HttpStatusCode.NoContent) {
            return true
        } else {
            println("Invalid request. Please try again.")
            return false
        }
    }

    suspend fun login(loginRequest: LoginRequest): String? {
        return try {
            val response: HttpResponse = client.post("$baseUrl/authenticate") {
                setBody(loginRequest)
            }

            if (response.status == HttpStatusCode.Unauthorized) {
                println("Login failed: Invalid credentials (401). Please try again.")
                return null
            }

            if (response.status == HttpStatusCode.OK) {
                val loginResponse: LoginResponse = response.body()
                return loginResponse.id_token
            } else {
                println("Login failed. Please try again.")
                return null
            }
        } catch (e: Exception) {
            println("Login failed: ${e.message}")
            return null
        }
    }
}