package org.adastra.curriculum.helper

class ExpertiseHelper {
    fun getExpertise(expertise: Int): String {
        if (expertise == 1) return "Základní znalost"
        if (expertise == 2) return "Lepší znalost"
        if (expertise == 3) return "Pokročilá znalost"
        if (expertise == 4) return "Rozšířená znalost"
        if (expertise == 5) return "Seniorní znalost"
        return "-"
    }
}