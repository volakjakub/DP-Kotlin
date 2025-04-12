package org.adastra.curriculum.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.adastra.curriculum.BuildConfig
import org.adastra.curriculum.client.BackendApi
import org.adastra.curriculum.client.data.AccountResponse
import org.adastra.curriculum.client.data.LoginRequest

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val baseUrl = BuildConfig.BASE_URL
    private val tokenManagerProvider = TokenManagerProvider(
        context = application.applicationContext
    )
    private val tokenManager = tokenManagerProvider.createTokenManager()
    private val backendApi = BackendApi(baseUrl)

    // LiveData to manage token validity
    private val _isLoggedIn = MutableLiveData<Boolean>()

    fun isLoggedIn(): LiveData<Boolean> = _isLoggedIn

    // Check if the token is valid
    fun checkToken() {
        _isLoggedIn.value = tokenManager.getToken() != null
    }

    fun getToken(): String? {
        return tokenManager.getToken()
    }

    fun getAccount(): AccountResponse? {
        return tokenManager.getAccount()
    }

    // Handle login
    suspend fun login(loginRequest: LoginRequest): Boolean {
        var token = backendApi.login(loginRequest)
        if (token != null) {
            tokenManager.saveToken(token)
            val account = backendApi.getAccount(token)
            if (account != null) {
                tokenManager.saveAccount(account)
                _isLoggedIn.value = true
                return true
            } else {
                _isLoggedIn.value = false
                return false
            }
        } else {
            _isLoggedIn.value = false
            return false
        }
    }

    // Handle logout
    fun logout() {
        tokenManager.clearToken()
        tokenManager.clearAccount()
        _isLoggedIn.value = false
    }
}