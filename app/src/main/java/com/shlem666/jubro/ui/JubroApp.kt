package com.shlem666.jubro.ui

import android.content.pm.ActivityInfo
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.imePadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.window.core.layout.WindowWidthSizeClass
import com.shlem666.jubro.feature.settings.SettingsDialog
import com.shlem666.jubro.ui.toolbars.BottomToolBarLayout
import com.shlem666.jubro.ui.toolbars.LeftToolBarLayout
import com.shlem666.jubro.ui.toolbars.RightToolBarLayout
import com.shlem666.jubro.ui.toolbars.TopToolBarLayout
import com.shlem666.jubro.ui.UiState.Loading
import com.shlem666.jubro.ui.UiState.Success

@Composable
fun JubroApp(
    viewModel: JubroViewModel = hiltViewModel(),
    hideStatusBar: () -> Unit,
    showStatusBar: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var notchPadding by rememberSaveable { mutableStateOf(false) }
    var hideStatusBar by rememberSaveable { mutableStateOf(false) }
    var screenOrient by rememberSaveable { mutableIntStateOf(
        ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    ) }

    var showSettingsDialog by rememberSaveable { mutableStateOf(false) }
    if (showSettingsDialog) {
        SettingsDialog(
            onDismiss = { showSettingsDialog = false },
        )
    }

    when (uiState) {
        is Loading -> { }
        is Success -> {
            notchPadding = (uiState as Success)
                .resources.notchPadding
            hideStatusBar = (uiState as Success)
                .resources.hideStatusBarInLandscape
            screenOrient = (uiState as Success)
                .resources.screenOrient
        }
    }

    val portrait = currentWindowAdaptiveInfo().windowSizeClass
        .windowWidthSizeClass == WindowWidthSizeClass.COMPACT
    if (portrait || !hideStatusBar) { showStatusBar() }
    if (!portrait && hideStatusBar) { hideStatusBar() }

    @Composable
    fun LockScreenOrientation(orientation: Int) {
        val activity = LocalActivity.current
        DisposableEffect(orientation) {
            val activity = activity ?: return@DisposableEffect onDispose { }
            val originalOrientation = activity.requestedOrientation
            activity.requestedOrientation = orientation
            onDispose {
                // restore original orientation when view disappears
                activity.requestedOrientation = originalOrientation
            }
        }
    }

    LockScreenOrientation(screenOrient)

    Scaffold(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background)
            .imePadding()
            .then(if (notchPadding) {
                Modifier.displayCutoutPadding()
            } else { Modifier } )
        ,
        topBar = {
            if (portrait) {
                TopToolBarLayout(
                    toggleSettingDialog = { showSettingsDialog = true }
                )
            }
        },
        bottomBar = { if (portrait) { BottomToolBarLayout() } }
    ) { innerPadding ->
        Row (
            Modifier.padding(innerPadding)
        ) {
            if (!portrait) { LeftToolBarLayout() }
            Box( modifier = Modifier.weight(1f) ) { JubroWebView() }
            if (!portrait) {
                RightToolBarLayout(
                    toggleSettingDialog = { showSettingsDialog = true }
                )
            }
        }
    }
}