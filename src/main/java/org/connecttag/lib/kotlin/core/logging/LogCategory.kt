package org.connecttag.lib.kotlin.core.logging

/**
 * Logical grouping for logs to help filtering.
 */
enum class LogCategory(val tag: String) {
    Lifecycle("LIFECYCLE"),
    Network("NETWORK"),
    Database("DATABASE"),
    Auth("AUTH"),
    Ui("UI"),
    Security("SECURITY"),
    Action("ACTION"),
    Mvi("MVI"),
    General("GENERAL"),
}
