package org.connecttag.lib.kotlin.core.util

fun String.lowercaseStable(): String = this.lowercase()
fun String.uppercaseStable(): String = this.uppercase()

val String?.isArabicLanguage: Boolean
    get() = this?.trim()?.lowercaseStable()?.let { it == "ar" || it == "arabic" } ?: false

val String?.isEnglishLanguage: Boolean
    get() = this?.trim()?.lowercaseStable()?.let { it == "en" || it == "english" } ?: false
