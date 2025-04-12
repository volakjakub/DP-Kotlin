package org.adastra.curriculum.service

import org.adastra.curriculum.BuildConfig
import org.adastra.curriculum.auth.LoginViewModel
import org.adastra.curriculum.client.BackendApi
import org.adastra.curriculum.client.data.AccountRequest
import org.adastra.curriculum.client.data.AccountResponse

class UserService(loginViewModel: LoginViewModel) {
    private val baseUrl = BuildConfig.BASE_URL
    private val backendApi = BackendApi(baseUrl)
    private val lvm = loginViewModel
    private val token = lvm.getToken()
    private val username = lvm.getAccount()?.login

    suspend fun getUsers(page: Int = 0, size: Int = 10): List<AccountResponse> {
        var users: List<AccountResponse> = emptyList()
        if (token == null || username == null) {
            lvm.logout()
            throw Exception("Chyba při načítání dat. Přihlaste se prosím znovu.")
        } else {
            try {
                users = backendApi.getUsers(token, page, size)
            } catch (e: IllegalStateException) {
                lvm.logout()
                throw Exception(e.message)
            }
        }

        return users
    }

    suspend fun createUser(accountRequest: AccountRequest): AccountResponse {
        var account: AccountResponse? = null
        if (token == null || username == null) {
            lvm.logout()
            throw Exception("Chyba při načítání dat. Přihlaste se prosím znovu.")
        } else {
            try {
                account = backendApi.saveUser(token, accountRequest, null)
            } catch (e: IllegalStateException) {
                lvm.logout()
                throw Exception(e.message)
            }
        }

        if (account == null) {
            throw Exception("Chyba při ukládání dat. Zkontrolujte prosím zadané údaje.")
        } else {
            return account
        }
    }

    suspend fun updateUser(accountRequest: AccountRequest): AccountResponse {
        var account: AccountResponse? = null
        if (token == null || username == null) {
            lvm.logout()
            throw Exception("Chyba při načítání dat. Přihlaste se prosím znovu.")
        } else {
            try {
                account = backendApi.saveUser(token, accountRequest, accountRequest.id)
            } catch (e: IllegalStateException) {
                lvm.logout()
                throw Exception(e.message)
            }
        }

        if (account == null) {
            throw Exception("Chyba při ukládání dat. Zkontrolujte prosím zadané údaje.")
        } else {
            return account
        }
    }
}