package org.adastra.curriculum.helper

class LanguageHelper {
    fun getLanguage(language: String): String {
        if (language == "CZECH") return "Čeština"
        if (language == "SLOVAK") return "Slovenština"
        if (language == "ENGLISH") return "Angličtina"
        return "-"
    }
}