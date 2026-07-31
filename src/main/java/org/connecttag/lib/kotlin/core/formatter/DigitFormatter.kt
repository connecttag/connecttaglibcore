package org.connecttag.lib.kotlin.core.formatter

object DigitFormatter {
    fun normalizeToLatinDigits(input: String): String =
        input.mapNotNull { it.toLatinDigitOrNull() }.joinToString(separator = "")

    fun renderDigits(input: String, digitStyle: DigitStyle): String {
        val symbols = digitStyle.symbols()
        if (symbols.zero == '0') return input

        return buildString(input.length) {
            input.forEach { char ->
                append(
                    if (char in '0'..'9') {
                        (symbols.zero.code + (char.code - '0'.code)).toChar()
                    } else {
                        char
                    },
                )
            }
        }
    }
}

internal fun Char.toLatinDigitOrNull(): Char? =
    when (this) {
        in '0'..'9' -> this
        in '\u0660'..'\u0669' -> ('0'.code + (code - '\u0660'.code)).toChar()
        in '\u06f0'..'\u06f9' -> ('0'.code + (code - '\u06f0'.code)).toChar()
        else -> null
    }
