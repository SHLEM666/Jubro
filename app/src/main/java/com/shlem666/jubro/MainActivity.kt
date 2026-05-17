package com.shlem666.jubro

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import dagger.hilt.android.AndroidEntryPoint
import com.shlem666.jubro.core.designsystem.theme.JubroTheme
import com.shlem666.jubro.ui.JubroApp
import com.shlem666.jubro.feature.settings.SettingsViewModel
import com.shlem666.jubro.feature.settings.SettingsUiState.Success

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // We keep this as a mutable state, so that we can
        // track changes inside the composition.
        // This allows us to react to dark/light mode changes.
        var darkTheme by mutableStateOf(false)

        // Update the uiState
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.settingsUiState.collect { uiState ->
                    if (uiState is Success) {
                        darkTheme = uiState.appSettings.darkTheme
                        enableEdgeToEdge(
                            statusBarStyle = SystemBarStyle.auto(
                                lightScrim = android.graphics.Color.TRANSPARENT,
                                darkScrim = android.graphics.Color.TRANSPARENT,
                            ) { darkTheme },
                            navigationBarStyle = SystemBarStyle.auto(
                                lightScrim = android.graphics.Color.TRANSPARENT,
                                darkScrim = android.graphics.Color.TRANSPARENT,
                            ) { darkTheme },
                        )
                    }
                }
            }
        }

        setContent {
            JubroTheme(darkTheme = darkTheme) {
                JubroApp()
            }
        }
    }

    override fun getApplicationContext(): Context? {
        return super.getApplicationContext()
    }
}