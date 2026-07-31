package org.connecttag.lib.kotlin.core.utils

import android.util.Patterns

/**
 * Generic interface for input validation.
 */
interface Validator<T> {
    fun validate(value: T): ValidationResult
}

/**
 * Result of a validation operation.
 */
sealed class ValidationResult {
    data object Success : ValidationResult()
    data class Failure(val messageKey: Int) : ValidationResult()
}

/**
 * Validates email addresses using Android's [Patterns.EMAIL_ADDRESS].
 */
class EmailValidator(private val errorRes: Int) : Validator<String?> {
    override fun validate(value: String?): ValidationResult {
        return if (!value.isNullOrBlank() && Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
            ValidationResult.Success
        } else {
            ValidationResult.Failure(errorRes)
        }
    }
}

/**
 * Validates phone numbers using Android's [Patterns.PHONE].
 */
class PhoneValidator(private val errorRes: Int) : Validator<String?> {
    override fun validate(value: String?): ValidationResult {
        return if (!value.isNullOrBlank() && Patterns.PHONE.matcher(value).matches()) {
            ValidationResult.Success
        } else {
            ValidationResult.Failure(errorRes)
        }
    }
}

/**
 * Validates password strength (minimum length by default).
 */
class PasswordValidator(
    private val minLength: Int = 8,
    private val errorRes: Int
) : Validator<String?> {
    override fun validate(value: String?): ValidationResult {
        return if (!value.isNullOrBlank() && value.length >= minLength) {
            ValidationResult.Success
        } else {
            ValidationResult.Failure(errorRes)
        }
    }
}
