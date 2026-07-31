package org.connecttag.lib.kotlin.core.actions.internal

/** Stable runtime contract for enum values crossing network or persistence boundaries. */
interface WireValueEnum {
    val wireValue: String
}
