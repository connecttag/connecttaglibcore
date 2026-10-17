package org.connecttag.lib.kotlin.core.util

/**
 * Single Source of Truth (SSOT) for Arabic text normalization, Tashkeel/Tatweel stripping,
 * and fuzzy Arabic search comparison across ConnectTag projects.
 */
object ArabicStringUtils {

    private val TASHKEEL_REGEX = Regex("[\\u064B-\\u065F\\u0670\\u06D6-\\u06ED]")
    private val TATWEEL_REGEX = Regex("\\u0640")

    /**
     * Strips standard Arabic diacritics (Harakat / Tashkeel) using Unicode range.
     */
    fun removeTashkeel(input: String?): String {
        if (input.isNullOrBlank()) return ""
        return input.replace(TASHKEEL_REGEX, "")
    }

    /**
     * Strips Tatweel (Kashida) character.
     */
    fun removeTatweel(input: String?): String {
        if (input.isNullOrBlank()) return ""
        return input.replace(TATWEEL_REGEX, "")
    }

    /**
     * Comprehensive Arabic text normalizer.
     * Removes diacritics, Tatweel, normalizes Alef variants (أ, إ, آ, ٱ -> ا),
     * Taa Marbuta (ة -> ه), Yaa / Alef Maksura (ى -> ي), and Persian/Urdu characters.
     */
    fun normalize(input: String?): String {
        if (input.isNullOrBlank()) return ""

        return input.trim()
            .replace(TASHKEEL_REGEX, "")
            .replace(TATWEEL_REGEX, "")
            .replace('أ', 'ا')
            .replace('إ', 'ا')
            .replace('آ', 'ا')
            .replace('ٱ', 'ا')
            .replace('ة', 'ه')
            .replace('ى', 'ي')
            .replace('ك', 'ك')
            .replace('ک', 'ك')
            .replace('ی', 'ي')
            .lowercase()
    }

    /**
     * Checks if a target string contains the query using normalized Arabic comparison.
     */
    fun matches(target: String?, query: String?): Boolean {
        if (query.isNullOrBlank()) return true
        if (target.isNullOrBlank()) return false

        val normalizedTarget = normalize(target)
        val normalizedQuery = normalize(query)

        return normalizedTarget.contains(normalizedQuery)
    }

    // =========================================================================
    // Legacy Compatibility Functions
    // =========================================================================

    fun replaceLetters(input: String): String {
        var output = input.replace("أ".toRegex(), "ا")
        output = output.replace("إ".toRegex(), "ا")
        output = output.replace("ى".toRegex(), "ي")
        output = output.replace("ة".toRegex(), "ه")
        output = output.replace("آ".toRegex(), "ا")
        output = output.replace("ٱ".toRegex(), "ا")
        return output
    }

    fun removeArHamzh(txt: String): String {
        var output = txt.replace("أ".toRegex(), "ا")
        output = output.replace("آ".toRegex(), "ا")
        output = output.replace("إ".toRegex(), "ا")
        output = output.replace("ٱ".toRegex(), "ا")
        output = output.replace("ة".toRegex(), "ه")
        return output
    }

    fun removeArTashkel(txt: String): String {
        return removeTashkeel(txt)
    }

    fun replaceArSomeWord(txt: String): String {
        var output = txt.replace("  ".toRegex(), " ")
        output = output.replace("أ".toRegex(), "ا")
        output = output.replace("آ".toRegex(), "ا")
        output = output.replace("إ".toRegex(), "ا")
        output = output.replace("ٱ".toRegex(), "ا")
        output = output.replace("ئ".toRegex(), "ي")
        output = output.replace("ى".toRegex(), "ي")
        output = output.replace("ؤ".toRegex(), "و")
        output = output.replace("ة".toRegex(), "ه")
        output = output.replace("ال".toRegex(), "")
        output = output.replace("ء".toRegex(), "")
        return output
    }

    fun replaceArabicTaWithHa2(text: String): String {
        return text.split(" ").joinToString(" ") { word ->
            val lastCharIndex = word.length - 1
            if (lastCharIndex >= 0 && word[lastCharIndex] == 'ة') {
                word.substring(0, lastCharIndex) + 'ه'
            } else {
                word
            }
        }
    }
}
