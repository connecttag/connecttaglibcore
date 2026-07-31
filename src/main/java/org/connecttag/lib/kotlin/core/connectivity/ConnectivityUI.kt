package org.connecttag.lib.kotlin.core.connectivity

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.connecttag.lib.kotlin.core.R

@Composable
fun NetworkStatusIndicator(
    connectivitySnapshot: ConnectivitySnapshot,
    modifier: Modifier = Modifier
) {
    val isAvailable = connectivitySnapshot.isInternetAvailable
    val state = connectivitySnapshot.state

    AnimatedVisibility(
        visible = !isAvailable || state == ConnectivityState.Losing || state == ConnectivityState.Lost,
        enter = expandVertically() + fadeIn(),
        exit = shrinkVertically() + fadeOut(),
        modifier = modifier
    ) {
        val (backgroundColor, label) = when (state) {
            ConnectivityState.Unavailable -> Color(0xFFF44336) to stringResource(R.string.connectivity_status_offline)
            ConnectivityState.Losing -> Color(0xFFFFEB3B) to stringResource(R.string.connectivity_status_unstable)
            ConnectivityState.Lost -> Color(0xFFF44336) to stringResource(R.string.connectivity_status_connection_lost)
            else -> Color(0xFF4CAF50) to stringResource(R.string.connectivity_status_online)
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundColor.copy(alpha = 0.9f))
                .padding(vertical = 4.dp, horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = if (state == ConnectivityState.Losing) Color.Black else Color.White
            )
        }
    }
}
