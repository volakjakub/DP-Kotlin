package org.adastra.curriculum.client.wrapper

import org.adastra.curriculum.client.data.SkillRequest

class SkillRequestWrapper {
    fun createSkillRequest(
        name: String,
        expertise: Int
    ): SkillRequest = SkillRequest(
        null,
        name,
        expertise,
        null
    )

    fun updateSkillRequest(
        id: Int,
        name: String,
        expertise: Int
    ): SkillRequest = SkillRequest(
        id,
        name,
        expertise,
        null
    )
}