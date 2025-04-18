package org.adastra.curriculum.client.wrapper

import org.adastra.curriculum.client.data.BiographyRequest
import org.adastra.curriculum.client.data.BiographyUserRequest

class BiographyRequestWrapper {
    fun createBiographyRequest(
        title: String,
        firstName: String,
        lastName: String,
        phone: String,
        email: String,
        street: String,
        city: String,
        country: String,
        position: String,
        employedFrom: String,
        user: BiographyUserRequest
    ): BiographyRequest = BiographyRequest(
        null,
        title,
        firstName,
        lastName,
        email,
        phone,
        street,
        city,
        country,
        position,
        employedFrom,
        user
    )

    fun updateBiographyRequest(
        id: Int,
        title: String,
        firstName: String,
        lastName: String,
        phone: String,
        email: String,
        street: String,
        city: String,
        country: String,
        position: String,
        employedFrom: String,
        user: BiographyUserRequest
    ): BiographyRequest = BiographyRequest(
        id,
        title,
        firstName,
        lastName,
        email,
        phone,
        street,
        city,
        country,
        position,
        employedFrom,
        user
    )
}