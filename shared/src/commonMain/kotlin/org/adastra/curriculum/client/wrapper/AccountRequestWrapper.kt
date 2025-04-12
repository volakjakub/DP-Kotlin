package org.adastra.curriculum.client.wrapper

import org.adastra.curriculum.client.data.AccountRequest
import org.adastra.curriculum.client.data.Authority

class AccountRequestWrapper {
    fun createAccountRequest(
        login: String,
        firstName: String,
        lastName: String,
        email: String,
        activated: Boolean,
        langKey: String,
        authorities: List<Authority>
    ): AccountRequest = AccountRequest(
        null,
        login,
        firstName,
        lastName,
        email,
        activated,
        langKey,
        authorities
    )

    fun updateAccountRequest(
        id: Int,
        login: String,
        firstName: String,
        lastName: String,
        email: String,
        activated: Boolean,
        langKey: String,
        authorities: List<Authority>
    ): AccountRequest = AccountRequest(
        id,
        login,
        firstName,
        lastName,
        email,
        activated,
        langKey,
        authorities
    )
}