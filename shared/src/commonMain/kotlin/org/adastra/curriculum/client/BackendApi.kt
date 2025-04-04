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
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.adastra.curriculum.auth.TokenManager
import org.adastra.curriculum.client.data.LoginRequest
import org.adastra.curriculum.client.data.LoginResponse

class BackendApi(val baseUrl: String, private val tokenManager: TokenManager) {
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

    suspend fun login(loginRequest: LoginRequest): Boolean {
        return try {
            val response: HttpResponse = client.post("$baseUrl/authenticate") {
                setBody(loginRequest)
            }

            if (response.status == HttpStatusCode.Unauthorized) {
                println("Login failed: Invalid credentials (401). Please try again.")
                tokenManager.clearToken()
                return false
            }

            if (response.status == HttpStatusCode.OK) {
                val loginResponse: LoginResponse = response.body()
                tokenManager.saveToken(loginResponse.id_token)
                return true
            } else {
                println("Login failed. Please try again.")
                tokenManager.clearToken()
                return false
            }
        } catch (e: Exception) {
            println("Login failed: ${e.message}")
            tokenManager.clearToken()
            return false
        }
    }
}