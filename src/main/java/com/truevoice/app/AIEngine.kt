package com.truevoice.app

object AIEngine {

    fun analyze(name: String, number: String): String {

        val score = calculateScore(name, number)

        return when {
            score > 70 -> "SAFE"
            score > 40 -> "SUSPICIOUS"
            else -> "SPOOF"
        }
    }

    // ✅ NOW INSIDE OBJECT (correct)
    private fun calculateScore(name: String, number: String): Int {
        var score = 50

        if (name != "Unknown") score += 30
        if (number.length >= 10) score += 10
        if (number.endsWith("000")) score -= 40

        return score
    }
}