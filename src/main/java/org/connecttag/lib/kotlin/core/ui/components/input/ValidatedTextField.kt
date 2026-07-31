package org.connecttag.lib.kotlin.core.ui.components.input

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.VisualTransformation
import kotlinx.coroutines.launch
import org.connecttag.lib.kotlin.core.validator.ValidationRule
import org.connecttag.lib.kotlin.core.validator.ValidationResult

/**
 * A text field that performs validation using a list of [ValidationRule].
 */
@Composable
fun ValidatedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    rules: List<ValidationRule<String>>,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    enabled: Boolean = true,
    singleLine: Boolean = true,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    validateOnValueChange: Boolean = true,
) {
    var errorText by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    fun performValidation() {
        scope.launch {
            for (rule in rules) {
                if (rule.isEnabled()) {
                    val result = rule.validate(value)
                    if (result is ValidationResult.Invalid) {
                        errorText = result.error
                        return@launch
                    }
                }
            }
            errorText = null
        }
    }

    ConnectTagTextField(
        value = value,
        onValueChange = {
            onValueChange(it)
            if (validateOnValueChange) {
                // We don't perform heavy validation here to avoid UI jank,
                // but we clear errors immediately if user starts typing.
                errorText = null
            }
        },
        modifier = modifier.onFocusChanged { focusState ->
            if (!focusState.isFocused) {
                performValidation()
            }
        },
        label = label,
        placeholder = placeholder,
        supportingText = errorText,
        isError = errorText != null,
        enabled = enabled,
        singleLine = singleLine,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions
    )
}
