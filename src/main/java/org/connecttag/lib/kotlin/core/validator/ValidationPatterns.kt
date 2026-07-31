package org.connecttag.lib.kotlin.core.validator

object ValidationPatterns {
    val EMAIL = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$".toRegex()
    val PHONE = "^[0-9]{9,15}$".toRegex()
    val USERNAME = "^[a-zA-Z0-9_]{3,30}$".toRegex()
    val DECIMAL_NUMBER = Regex("""\d+(?:[.,]\d+)?""")
}
