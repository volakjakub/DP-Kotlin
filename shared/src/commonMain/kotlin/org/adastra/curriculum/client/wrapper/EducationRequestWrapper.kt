package org.adastra.curriculum.client.wrapper

import org.adastra.curriculum.client.data.EducationRequest

class EducationRequestWrapper {
    fun createEducationRequest(
        school: String,
        type: String,
        start: String,
        end: String?
    ): EducationRequest = EducationRequest(
        null,
        school,
        type,
        start,
        end,
        null
    )

    fun updateEducationRequest(
        id: Int,
        school: String,
        type: String,
        start: String,
        end: String?
    ): EducationRequest = EducationRequest(
        id,
        school,
        type,
        start,
        end,
        null
    )
}