package org.adastra.curriculum.client.wrapper

import org.adastra.curriculum.client.data.ProjectRequest
import org.adastra.curriculum.client.data.ProjectSkillRequest

class ProjectRequestWrapper {
    fun createProjectRequest(
        name: String,
        client: String,
        start: String,
        end: String?,
        desc: String?,
        skills: List<ProjectSkillRequest>
    ): ProjectRequest = ProjectRequest(
        null,
        name,
        client,
        start,
        end,
        desc,
        skills,
        null
    )

    fun updateProjectRequest(
        id: Int,
        name: String,
        client: String,
        start: String,
        end: String?,
        desc: String?,
        skills: List<ProjectSkillRequest>
    ): ProjectRequest = ProjectRequest(
        id,
        name,
        client,
        start,
        end,
        desc,
        skills,
        null
    )
}