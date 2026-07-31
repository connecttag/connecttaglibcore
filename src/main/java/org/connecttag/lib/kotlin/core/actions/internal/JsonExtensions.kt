package org.connecttag.lib.kotlin.core.actions.internal

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull

fun Json.parseJsonElementOrNull(raw: String?): JsonElement? {
    val text = raw?.trim()?.takeIf { it.isNotEmpty() } ?: return null
    return runCatching { parseToJsonElement(text) }.getOrNull()
}

fun JsonObject.stringValue(vararg keys: String): String? {
    for (key in keys) {
        val element = this[key]
        if (element is JsonPrimitive) {
            return element.contentOrNull?.trim()?.takeIf { it.isNotEmpty() }
        }
    }
    return null
}
