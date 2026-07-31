package org.connecttag.lib.kotlin.core.actions

import kotlinx.serialization.Serializable
import org.connecttag.lib.kotlin.core.actions.internal.WireValueEnum

@Serializable
enum class ActionType(override val wireValue: String) : WireValueEnum {
    NONE("NONE"),
    NAVIGATION("NAVIGATION"),
    SHOW_IMAGE_VIEW("SHOW_IMAGE_VIEW"),
    OPEN_URL("OPEN_URL"),
    OPEN_URL_IN_APP("OPEN_URL_IN_APP"),
    OPEN_WEBVIEW("OPEN_WEBVIEW"),
    OPEN_FILE("OPEN_FILE"),
    OPEN_ACTIVITY("OPEN_ACTIVITY"),
    OPEN_DIALOG("OPEN_DIALOG"),
    OPEN_BOTTOM_SHEET("OPEN_BOTTOM_SHEET"),
    OPEN_EXTERNAL_APP("OPEN_EXTERNAL_APP"),
    OPEN_DEEPLINK("OPEN_DEEPLINK"),
    OPEN_APP("OPEN_APP"),
    CELL_PHONE("CELL_PHONE"),
    SEND_EMAIL("SEND_EMAIL"),
    SEND_SMS("SEND_SMS"),
    SEND_WHATSAPP("SEND_WHATSAPP"),
    SEND_WHATSAPP_GROUP("SEND_WHATSAPP_GROUP"),
    OPEN_WHATSAPP_CHANNEL("OPEN_WHATSAPP_CHANNEL"),
    SEND_MESSENGER("SEND_MESSENGER"),
    SEND_TELEGRAM("SEND_TELEGRAM"),
    OPEN_SETTINGS("OPEN_SETTINGS"),
    OPEN_PLAY_STORE("OPEN_PLAY_STORE"),
    OPEN_APP_STORE("OPEN_APP_STORE"),
    RATE_APP("RATE_APP"),
    SHARE_APP("SHARE_APP"),
    SHARE_MESSAGE("SHARE_MESSAGE"),
    SHARE_PRODUCT_CARD("SHARE_PRODUCT_CARD"),
    SHOW_TOAST("SHOW_TOAST"),
    OPEN_POPUP_MENU("OPEN_POPUP_MENU"),
    COPY_TO_CLIPBOARD("COPY_TO_CLIPBOARD"),
    DOWNLOAD_FILE("DOWNLOAD_FILE"),
    OPEN_NOTIFICATION("OPEN_NOTIFICATION"),
    PERMISSION_REQUEST("PERMISSION_REQUEST"),
    CUSTOM_ACTION("CUSTOM_ACTION"),
    OPEN_MAP("OPEN_MAP"),
    OPEN_FACEBOOK("OPEN_FACEBOOK"),
    OPEN_X("OPEN_X"),
    OPEN_YOUTUBE("OPEN_YOUTUBE"),
    UPDATE_APP("UPDATE_APP"),
    CLEAR_CACHE("CLEAR_CACHE"),
    REQUEST_REVIEW("REQUEST_REVIEW"),
    TOGGLE_THEME("TOGGLE_THEME"),
    SHOW_DIALOG("SHOW_DIALOG"),
    SHOW_SNACKBAR("SHOW_SNACKBAR"),
    CLOSE_APP("CLOSE_APP"),
    ;

    companion object {
        fun fromWireValue(rawValue: String?): ActionType? {
            val canonical = rawValue?.takeIf(String::isNotBlank) ?: return null
            return entries.firstOrNull { it.wireValue == canonical }
        }
    }
}

@Serializable
enum class ActionSource(override val wireValue: String) : WireValueEnum {
    UI("UI"),
    SERVER_CONTENT("SERVER_CONTENT"),
    LOCAL_CACHE("LOCAL_CACHE"),
    SHORTCUT("SHORTCUT"),
    UNKNOWN("UNKNOWN"),
    ;

    companion object {
        val selectableOptions: List<ActionSource> = entries - UNKNOWN

        fun fromWireValue(rawValue: String?): ActionSource {
            val normalized = rawValue?.trim()?.uppercase()?.takeIf(String::isNotBlank) ?: return UNKNOWN
            return entries.firstOrNull { it.wireValue == normalized } ?: UNKNOWN
        }
    }
}

@Serializable
enum class ActionContentKind(val analyticsValue: String) {
    NONE("none"),
    TEXT("text"),
    URL("url"),
    ROUTE("route"),
    PHONE("phone"),
    EMAIL("email"),
    PACKAGE_NAME("package_name"),
    URI("uri"),
    MAP_QUERY("map_query"),
    JSON("json"),
}

data class ActionTypeSpec(
    val type: ActionType,
    val requiresContent: Boolean,
    val contentKind: ActionContentKind,
    val supported: Boolean = true,
    val requiresNavController: Boolean = false,
    val userFeedbackOnFailure: Boolean = true,
)

@Serializable
sealed interface ActionContent {
    val kind: ActionContentKind
    val rawValue: String?

    @Serializable
    data object None : ActionContent {
        override val kind: ActionContentKind = ActionContentKind.NONE
        override val rawValue: String? = null
    }

    @Serializable
    sealed interface Value : ActionContent {
        val value: String
        override val rawValue: String
            get() = value
    }

    @Serializable
    data class Text(override val value: String) : Value {
        override val kind: ActionContentKind = ActionContentKind.TEXT
    }

    @Serializable
    data class Url(override val value: String) : Value {
        override val kind: ActionContentKind = ActionContentKind.URL
    }

    @Serializable
    data class Route(override val value: String) : Value {
        override val kind: ActionContentKind = ActionContentKind.ROUTE
    }

    @Serializable
    data class Phone(override val value: String) : Value {
        override val kind: ActionContentKind = ActionContentKind.PHONE
    }

    @Serializable
    data class Email(override val value: String) : Value {
        override val kind: ActionContentKind = ActionContentKind.EMAIL
    }

    @Serializable
    data class PackageName(override val value: String) : Value {
        override val kind: ActionContentKind = ActionContentKind.PACKAGE_NAME
    }

    @Serializable
    data class Uri(override val value: String) : Value {
        override val kind: ActionContentKind = ActionContentKind.URI
    }

    @Serializable
    data class MapQuery(override val value: String) : Value {
        override val kind: ActionContentKind = ActionContentKind.MAP_QUERY
    }

    @Serializable
    data class Json(override val value: String) : Value {
        override val kind: ActionContentKind = ActionContentKind.JSON
    }
}

@Serializable
data class ActionRequest(
    val type: ActionType,
    val payload: ActionContent,
    val source: ActionSource = ActionSource.UNKNOWN,
)

fun interface ActionRequestHandler {
    fun dispatch(request: ActionRequest)

    operator fun invoke(request: ActionRequest) = dispatch(request)

    operator fun invoke(
        type: ActionType,
        content: String? = null,
        source: ActionSource = ActionSource.UI,
    ) = dispatch(ActionRequestFactory.from(type, content, source))
}

sealed interface ActionResult {
    val handled: Boolean

    data object Handled : ActionResult {
        override val handled: Boolean = true
    }

    data object Ignored : ActionResult {
        override val handled: Boolean = false
    }

    data class Failed(val reason: ActionFailureReason) : ActionResult {
        override val handled: Boolean = false
    }
}

object ActionResults {
    val handled: ActionResult = ActionResult.Handled
    val ignored: ActionResult = ActionResult.Ignored

    fun failed(reason: ActionFailureReason): ActionResult =
        ActionResult.Failed(reason)

    fun failureReasonOrNull(result: ActionResult): ActionFailureReason? =
        (result as? ActionResult.Failed)?.reason
}

sealed interface ActionFailureReason {
    data object MissingContent : ActionFailureReason
    data object InvalidContent : ActionFailureReason
    data class ContentTypeMismatch(
        val expected: ActionContentKind,
        val actual: ActionContentKind,
    ) : ActionFailureReason
    data object MissingNavigationController : ActionFailureReason
    data object NoAppCanHandleIntent : ActionFailureReason
    data object BlockedByPolicy : ActionFailureReason
    data class UnsupportedActionType(val actionType: ActionType?) : ActionFailureReason
    data class ExceptionThrown(val message: String?) : ActionFailureReason

    fun toLogMessage(): String {
        return when (this) {
            MissingContent -> "Missing content"
            InvalidContent -> "Invalid content"
            is ContentTypeMismatch -> "Action content type mismatch: expected $expected, received $actual"
            MissingNavigationController -> "Missing navigation controller"
            NoAppCanHandleIntent -> "No app can handle intent"
            BlockedByPolicy -> "Blocked by policy"
            is UnsupportedActionType -> "Unsupported action type: $actionType"
            is ExceptionThrown -> "Exception: $message"
        }
    }
}

enum class ActionFailureKind {
    MISSING_CONTENT,
    INVALID_CONTENT,
    PAYLOAD_TYPE_MISMATCH,
    MISSING_NAVIGATION_CONTROLLER,
    NO_APP_CAN_HANDLE_INTENT,
    BLOCKED_BY_POLICY,
    UNSUPPORTED_ACTION_TYPE,
    EXCEPTION_THROWN,
}

object ActionFailureReasons {
    val missingContent: ActionFailureReason = ActionFailureReason.MissingContent
    val invalidContent: ActionFailureReason = ActionFailureReason.InvalidContent
    val missingNavigationController: ActionFailureReason = ActionFailureReason.MissingNavigationController
    val noAppCanHandleIntent: ActionFailureReason = ActionFailureReason.NoAppCanHandleIntent
    val blockedByPolicy: ActionFailureReason = ActionFailureReason.BlockedByPolicy

    fun unsupportedActionType(actionType: ActionType?): ActionFailureReason =
        ActionFailureReason.UnsupportedActionType(actionType)

    fun exceptionThrown(message: String?): ActionFailureReason =
        ActionFailureReason.ExceptionThrown(message)

    fun kindOf(reason: ActionFailureReason): ActionFailureKind =
        when (reason) {
            ActionFailureReason.MissingContent -> ActionFailureKind.MISSING_CONTENT
            ActionFailureReason.InvalidContent -> ActionFailureKind.INVALID_CONTENT
            is ActionFailureReason.ContentTypeMismatch -> ActionFailureKind.PAYLOAD_TYPE_MISMATCH
            ActionFailureReason.MissingNavigationController -> ActionFailureKind.MISSING_NAVIGATION_CONTROLLER
            ActionFailureReason.NoAppCanHandleIntent -> ActionFailureKind.NO_APP_CAN_HANDLE_INTENT
            ActionFailureReason.BlockedByPolicy -> ActionFailureKind.BLOCKED_BY_POLICY
            is ActionFailureReason.UnsupportedActionType -> ActionFailureKind.UNSUPPORTED_ACTION_TYPE
            is ActionFailureReason.ExceptionThrown -> ActionFailureKind.EXCEPTION_THROWN
        }
}
