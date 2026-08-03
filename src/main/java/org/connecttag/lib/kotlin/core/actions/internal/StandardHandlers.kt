package org.connecttag.lib.kotlin.core.actions.internal

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import org.connecttag.lib.kotlin.core.actions.*

/**
 * Standard implementation of action logic.
 */
object StandardHandlers {

    fun handleUrl(request: ActionRequest, env: ActionEnvironment): ActionResult {
        val url = request.payload.rawValue ?: return ActionResults.failed(ActionFailureReason.MissingContent)
        return try {
            val builder = CustomTabsIntent.Builder()
            builder.setShowTitle(true)
            val customTabsIntent = builder.build()
            customTabsIntent.launchUrl(env.context, Uri.parse(url))
            ActionResults.handled
        } catch (e: Exception) {
            // Fallback to external browser
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                env.context.startActivity(intent)
                ActionResults.handled
            } catch (e2: Exception) {
                ActionResults.failed(ActionFailureReason.ExceptionThrown(e2.message))
            }
        }
    }

    fun handlePhone(request: ActionRequest, env: ActionEnvironment): ActionResult {
        val phone = request.payload.rawValue ?: return ActionResults.failed(ActionFailureReason.MissingContent)
        return try {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone")).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            env.context.startActivity(intent)
            ActionResults.handled
        } catch (e: Exception) {
            ActionResults.failed(ActionFailureReason.ExceptionThrown(e.message))
        }
    }

    fun handleWhatsApp(request: ActionRequest, env: ActionEnvironment): ActionResult {
        val value = request.payload.rawValue ?: return ActionResults.failed(ActionFailureReason.MissingContent)
        val url = if (value.startsWith("http")) value else "https://wa.me/$value"
        return try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            env.context.startActivity(intent)
            ActionResults.handled
        } catch (e: Exception) {
            ActionResults.failed(ActionFailureReason.ExceptionThrown(e.message))
        }
    }

    fun handleTelegram(request: ActionRequest, env: ActionEnvironment): ActionResult {
        val value = request.payload.rawValue ?: return ActionResults.failed(ActionFailureReason.MissingContent)
        val url = if (value.startsWith("http")) value else "https://t.me/$value"
        return try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            env.context.startActivity(intent)
            ActionResults.handled
        } catch (e: Exception) {
            ActionResults.failed(ActionFailureReason.ExceptionThrown(e.message))
        }
    }

    fun handleEmail(request: ActionRequest, env: ActionEnvironment): ActionResult {
        val email = request.payload.rawValue ?: return ActionResults.failed(ActionFailureReason.MissingContent)
        return try {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:$email")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            env.context.startActivity(intent)
            ActionResults.handled
        } catch (e: Exception) {
            ActionResults.failed(ActionFailureReason.ExceptionThrown(e.message))
        }
    }

    fun handleClipboard(request: ActionRequest, env: ActionEnvironment): ActionResult {
        val text = request.payload.rawValue ?: return ActionResults.failed(ActionFailureReason.MissingContent)
        return try {
            val clipboard = env.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Copied Text", text)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(env.context, "تم النسخ إلى الحافظة", Toast.LENGTH_SHORT).show()
            ActionResults.handled
        } catch (e: Exception) {
            ActionResults.failed(ActionFailureReason.ExceptionThrown(e.message))
        }
    }
    
    fun handleNavigation(request: ActionRequest, env: ActionEnvironment): ActionResult {
        val route = request.payload.rawValue ?: return ActionResults.failed(ActionFailureReason.MissingContent)
        val navController = env.navController ?: return ActionResults.failed(ActionFailureReason.MissingNavigationController)
        return try {
            navController.navigate(route)
            ActionResults.handled
        } catch (e: Exception) {
            ActionResults.failed(ActionFailureReason.ExceptionThrown(e.message))
        }
    }
}
