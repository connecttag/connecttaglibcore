package org.connecttag.lib.kotlin.core.validator

interface ValidationRule<T> {
    suspend fun validate(value: T): ValidationResult
    fun isEnabled(): Boolean = true
}
