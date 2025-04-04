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
import org.adastra.curriculum.client.data.LoginRequest
import org.adastra.curriculum.client.data.LoginResponse

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