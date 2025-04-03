package org.adastra.curriculum

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform