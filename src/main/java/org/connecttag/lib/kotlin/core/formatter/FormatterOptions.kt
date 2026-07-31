package org.connecttag.lib.kotlin.core.formatter

import org.connecttag.lib.kotlin.core.util.isArabicLanguage

enum class FormatterLanguage {
    Arabic,
    English,
}

enum class DigitStyle {
    Latin,
    ArabicIndic,
    EasternArabicIndic,
}

data class FormatterOptions(
    val language: FormatterLanguage = FormatterLanguage.Arabic,
    val digitStyle: DigitStyle = DigitStyle.ArabicIndic,
) {
    companion object {
        fun arabic(
            digitStyle: DigitStyle = DigitStyle.ArabicIndic,
        ): FormatterOptions = FormatterOptions(
            language = FormatterLanguage.Arabic,
            digitStyle = digitStyle,
        )

        fun english(
            digitStyle: DigitStyle = DigitStyle.Latin,
        ): FormatterOptions = FormatterOptions(
            language = FormatterLanguage.English,
            digitStyle = digitStyle,
        )
    }
}

internal data class NumberSymbols(
    val zero: Char,
    val groupingSeparator: Char,
    val decimalSeparator: Char,
)

internal fun DigitStyle.symbols(): NumberSymbols =
    when (this) {
        DigitStyle.Latin -> NumberSymbols('0', ',', '.')
        DigitStyle.ArabicIndic -> NumberSymbols('\u0660', '\u066c', '\u066b')
        DigitStyle.EasternArabicIndic -> NumberSymbols('\u06f0', '\u066c', '\u066b')
    }
