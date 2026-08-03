package org.connecttag.lib.kotlin.core.actions

import android.content.Context
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope

/**
 * Runtime environment for executing actions.
 * Provides necessary Android components to handlers.
 */
data class ActionEnvironment(
    val context: Context,
    val navController: NavController? = null,
    val scope: CoroutineScope? = null
)
