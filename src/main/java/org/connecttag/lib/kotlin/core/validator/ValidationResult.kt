package org.connecttag.lib.kotlin.core.validator

data class ValidationError(
    val code: String,
    val messageKey: String,
    val fallbackMessage: String = "",
    val args: Map<String, String> = emptyMap(),
)

sealed interface ValidationResult {
    data object Valid : ValidationResult
    data class Invalid(val validationError: ValidationError) : ValidationResult {
        constructor(errorMessage: String) : this(
            ValidationError(
                code = ValidationErrorCodes.CUSTOM,
                messageKey = ValidationMessageKeys.CUSTOM,
                fallbackMessage = errorMessage,
            ),
        )

        val errorMessage: String get() = validationError.fallbackMessage
    }

    val isValid: Boolean get() = this is Valid
    val error: String? get() = (this as? Invalid)?.errorMessage
    val errorKey: String? get() = (this as? Invalid)?.validationError?.messageKey
    val errorDetails: ValidationError? get() = (this as? Invalid)?.validationError
}

object ValidationErrorCodes {
    const val REQUIRED = "REQUIRED"
    const val INVALID_EMAIL = "INVALID_EMAIL"
    const val MIN_LENGTH = "MIN_LENGTH"
    const val INVALID_PHONE = "INVALID_PHONE"
    const val CUSTOM = "CUSTOM"
}

object ValidationMessageKeys {
    const val REQUIRED = "validation.required"
    const val INVALID_EMAIL = "validation.email.invalid"
    const val MIN_LENGTH = "validation.length.min"
    const val INVALID_PHONE = "validation.phone.invalid"
    const val CUSTOM = "validation.custom"
}

object ValidationErrors {
    fun required(fallbackMessage: String = ""): ValidationError = ValidationError(
        code = ValidationErrorCodes.REQUIRED,
        messageKey = ValidationMessageKeys.REQUIRED,
        fallbackMessage = fallbackMessage,
    )

    fun invalidEmail(fallbackMessage: String = ""): ValidationError = ValidationError(
        code = ValidationErrorCodes.INVALID_EMAIL,
        messageKey = ValidationMessageKeys.INVALID_EMAIL,
        fallbackMessage = fallbackMessage,
    )

    fun minLength(length: Int, fallbackMessage: String = ""): ValidationError = ValidationError(
        code = ValidationErrorCodes.MIN_LENGTH,
        messageKey = ValidationMessageKeys.MIN_LENGTH,
        fallbackMessage = fallbackMessage,
        args = mapOf("length" to length.toString()),
    )

    fun invalidPhone(fallbackMessage: String = ""): ValidationError = ValidationError(
        code = ValidationErrorCodes.INVALID_PHONE,
        messageKey = ValidationMessageKeys.INVALID_PHONE,
        fallbackMessage = fallbackMessage,
    )

    fun custom(fallbackMessage: String = ""): ValidationError = ValidationError(
        code = ValidationErrorCodes.CUSTOM,
        messageKey = ValidationMessageKeys.CUSTOM,
        fallbackMessage = fallbackMessage,
    )
}
