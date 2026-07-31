package org.connecttag.lib.kotlin.core.validator

class RequiredValidator(
    private val validationError: ValidationError = ValidationErrors.required(),
) : ValidationRule<String> {
    constructor(message: String) : this(ValidationErrors.required(fallbackMessage = message))

    override suspend fun validate(value: String): ValidationResult {
        return if (value.isNotBlank()) ValidationResult.Valid else ValidationResult.Invalid(validationError)
    }
}

class EmailValidator(
    private val validationError: ValidationError = ValidationErrors.invalidEmail(),
) : ValidationRule<String> {
    constructor(message: String) : this(ValidationErrors.invalidEmail(fallbackMessage = message))

    override suspend fun validate(value: String): ValidationResult {
        return if (value.isEmpty() || ValidationPatterns.EMAIL.matches(value)) {
            ValidationResult.Valid
        } else {
            ValidationResult.Invalid(validationError)
        }
    }
}

class MinLengthValidator(
    private val length: Int,
    private val validationError: ValidationError,
) : ValidationRule<String> {
    constructor(length: Int, message: String? = null) : this(
        length = length,
        validationError = ValidationErrors.minLength(length, fallbackMessage = message.orEmpty()),
    )

    override suspend fun validate(value: String): ValidationResult {
        return if (value.length >= length) {
            ValidationResult.Valid
        } else {
            ValidationResult.Invalid(validationError)
        }
    }
}

class PhoneValidator(
    private val validationError: ValidationError = ValidationErrors.invalidPhone(),
) : ValidationRule<String> {
    constructor(message: String) : this(ValidationErrors.invalidPhone(fallbackMessage = message))

    override suspend fun validate(value: String): ValidationResult {
        return if (value.isEmpty() || ValidationPatterns.PHONE.matches(value)) {
            ValidationResult.Valid
        } else {
            ValidationResult.Invalid(validationError)
        }
    }
}

class CustomValidator<T>(
    private val validationError: ValidationError,
    private val enabled: () -> Boolean = { true },
    private val predicate: (T) -> Boolean
) : ValidationRule<T> {
    constructor(
        message: String,
        enabled: () -> Boolean = { true },
        predicate: (T) -> Boolean,
    ) : this(
        validationError = ValidationErrors.custom(fallbackMessage = message),
        enabled = enabled,
        predicate = predicate,
    )

    override fun isEnabled(): Boolean = enabled()
    override suspend fun validate(value: T): ValidationResult {
        return if (predicate(value)) ValidationResult.Valid else ValidationResult.Invalid(validationError)
    }
}
