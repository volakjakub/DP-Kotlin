package org.adastra.curriculum.helper

class EducationHelper {
    fun getType(type: String): String {
        if (type == "HIGH_SCHOOL") return "Maturitní vzdělání"
        if (type == "BACHELOR") return "Bakalářské vzdělání"
        if (type == "MASTER") return "Magisterské vzdělání"
        if (type == "DOCTORATE") return "Doktorské vzdělání"
        return "-"
    }
}