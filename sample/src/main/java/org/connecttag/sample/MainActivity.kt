package org.connecttag.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import org.connecttag.lib.kotlin.core.aboutapp.ConnectTagInfoScreen
import org.connecttag.lib.kotlin.core.theme.engine.ConnectTagTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ConnectTagTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ConnectTagInfoScreen(
                        onBack = { finish() },
                        onUrlClick = { /* Handle URL */ }
                    )
                }
            }
        }
    }
}
