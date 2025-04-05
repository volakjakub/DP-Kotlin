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
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.adastra.curriculum.client.data.AccountResponse
import org.adastra.curriculum.client.data.BiographyResponse
import org.adastra.curriculum.client.data.EducationResponse
import org.adastra.curriculum.client.data.LanguageResponse
import org.adastra.curriculum.client.data.LoginRequest
import org.adastra.curriculum.client.data.LoginResponse
import org.adastra.curriculum.client.data.ProjectResponse
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

    suspend fun getBiography(token: String, username: String): BiographyResponse? {
        val response: HttpResponse = client.get("$baseUrl/biographies/user?username=$username") {
            header(HttpHeaders.Authorization, "Bearer $token")
        }

        if (response.status == HttpStatusCode.Unauthorized) {
            println("Invalid credentials (401). Please log in.")
            throw IllegalStateException("Invalid credentials (401). Please log in.")
        }

        if (response.status == HttpStatusCode.OK) {
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