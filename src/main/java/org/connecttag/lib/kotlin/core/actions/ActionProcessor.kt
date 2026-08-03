package org.connecttag.lib.kotlin.core.actions

import org.connecttag.lib.kotlin.core.actions.internal.StandardHandlers
import org.connecttag.lib.kotlin.core.utils.logDebug

/**
 * Interface for the central action execution engine.
 */
interface ActionProcessor {
    /**
     * Executes the given action request within the provided environment.
     */
    fun process(request: ActionRequest, environment: ActionEnvironment): ActionResult
}

/**
 * Default implementation of [ActionProcessor].
 */
class ActionProcessorImpl : ActionProcessor {
    
    private val handlers = mutableMapOf<ActionType, (ActionRequest, ActionEnvironment) -> ActionResult>()

    init {
        registerHandler(ActionType.OPEN_URL, StandardHandlers::handleUrl)
        registerHandler(ActionType.OPEN_URL_IN_APP, StandardHandlers::handleUrl)
        registerHandler(ActionType.CELL_PHONE, StandardHandlers::handlePhone)
        registerHandler(ActionType.SEND_WHATSAPP, StandardHandlers::handleWhatsApp)
        registerHandler(ActionType.SEND_TELEGRAM, StandardHandlers::handleTelegram)
        registerHandler(ActionType.SEND_EMAIL, StandardHandlers::handleEmail)
        registerHandler(ActionType.COPY_TO_CLIPBOARD, StandardHandlers::handleClipboard)
        registerHandler(ActionType.NAVIGATION, StandardHandlers::handleNavigation)
    }

    /**
     * Registers a custom handler for a specific action type.
     */
    fun registerHandler(type: ActionType, handler: (ActionRequest, ActionEnvironment) -> ActionResult) {
        handlers[type] = handler
    }

    override fun process(request: ActionRequest, environment: ActionEnvironment): ActionResult {
        logDebug("ActionProcessor: Processing ${request.type} from ${request.source}")
        
        val handler = handlers[request.type]
        return if (handler != null) {
            try {
                handler(request, environment)
            } catch (e: Exception) {
                logDebug("ActionProcessor: Error executing ${request.type}: ${e.message}")
                ActionResults.failed(ActionFailureReason.ExceptionThrown(e.message))
            }
        } else {
            logDebug("ActionProcessor: No handler registered for ${request.type}")
            ActionResults.failed(ActionFailureReason.UnsupportedActionType(request.type))
        }
    }
}
