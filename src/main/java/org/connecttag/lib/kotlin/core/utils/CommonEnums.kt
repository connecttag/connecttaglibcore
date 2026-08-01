package org.connecttag.lib.kotlin.core.utils

import androidx.annotation.Keep

/**
 * Common alert types for UI notifications.
 */
@Keep
enum class AlertType {
    Danger,
    Warning,
    Caution,
    Notice,
    Safety,
    Normal
}

/**
 * Common log types.
 */
@Keep
enum class LogType {
    ObjectEvent,
    Upload
}

/**
 * Input types for dynamic forms or fields.
 */
@Keep
enum class InputType {
    String,
    Boolean,
    Integer,
    StringAndInteger,
    All
}
