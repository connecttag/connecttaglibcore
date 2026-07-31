package org.connecttag.lib.kotlin.core.formatter

enum class CurrencySymbolPosition {
    BeforeAmount,
    AfterAmount,
}

data class CurrencySpec(
    val code: String,
    val arabicSymbol: String,
    val englishSymbol: String = code,
    val minorUnit: Int,
    val symbolPosition: CurrencySymbolPosition = CurrencySymbolPosition.AfterAmount,
) {
    fun symbolFor(language: FormatterLanguage): String =
        when (language) {
            FormatterLanguage.Arabic -> arabicSymbol
            FormatterLanguage.English -> englishSymbol
        }
}

data class MoneyAmount(
    val minorUnits: Long,
    val currency: CurrencySpec,
)

data class MoneyFormatOptions(
    val formatterOptions: FormatterOptions = FormatterOptions.arabic(),
    val showCurrency: Boolean = true,
    val placeholder: String = "--",
    val trimTrailingFractionZeros: Boolean = true,
)

object MoneyFormatter {
    fun format(
        amount: MoneyAmount?,
        options: MoneyFormatOptions = MoneyFormatOptions(),
    ): String {
        if (amount == null) return options.placeholder

        val number = formatNumber(
            value = amount.minorUnits,
            minorUnit = amount.currency.minorUnit,
            digitStyle = options.formatterOptions.digitStyle,
            trimTrailingFractionZeros = options.trimTrailingFractionZeros,
        )

        if (!options.showCurrency) return number

        val symbol = amount.currency.symbolFor(options.formatterOptions.language)
        return when (amount.currency.symbolPosition) {
            CurrencySymbolPosition.BeforeAmount -> "$symbol $number"
            CurrencySymbolPosition.AfterAmount -> "$number $symbol"
        }
    }

    private fun formatNumber(
        value: Long,
        minorUnit: Int,
        digitStyle: DigitStyle,
        trimTrailingFractionZeros: Boolean,
    ): String {
        val symbols = digitStyle.symbols()
        val negative = value < 0
        val absolute = if (negative) -value else value
        val divisor = pow10(minorUnit)
        val major = if (minorUnit == 0) absolute else absolute / divisor
        val minor = if (minorUnit == 0) 0L else absolute % divisor

        val majorText = groupDigits(major.toString(), symbols.groupingSeparator)
        val fractionText = if (minorUnit == 0) {
            ""
        } else {
            val raw = minor.toString().padStart(minorUnit, '0')
            val normalized = if (trimTrailingFractionZeros) raw.trimEnd('0') else raw
            if (normalized.isEmpty()) "" else "${symbols.decimalSeparator}$normalized"
        }

        val sign = if (negative) "-" else ""
        return DigitFormatter.renderDigits("$sign$majorText$fractionText", digitStyle)
    }

    private fun groupDigits(value: String, separator: Char): String {
        if (value.length <= 3) return value
        return value
            .reversed()
            .chunked(3)
            .joinToString(separator = separator.toString())
            .reversed()
    }

    private fun pow10(exponent: Int): Long {
        var result = 1L
        repeat(exponent) { result *= 10L }
        return result
    }
}
