package com.shlem666.jubro

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.shlem666.jubro.core.designsystem.theme.JubroTheme
import com.shlem666.jubro.ui.JubroApp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JubroTheme {
                JubroApp(
                    hideStatusBar = { hideStatusBar() },
                    showStatusBar = { showStatusBar() },
                )
            }
        }
    }

    override fun getApplicationContext(): Context? {
        return super.getApplicationContext()
    }

    private fun hideStatusBar() {
        val windowInsetsController =
            WindowCompat.getInsetsController(window, window.decorView)

        windowInsetsController
            .hide(WindowInsetsCompat.Type.statusBars() )

        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }

    private fun showStatusBar() {
        val windowInsetsController =
            WindowCompat.getInsetsController(window, window.decorView)

        windowInsetsController
            .show(WindowInsetsCompat.Type.statusBars() )

        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_DEFAULT
    }
}