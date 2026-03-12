package com.shlem666.jubro.ui

import android.app.Activity
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
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.window.core.layout.WindowWidthSizeClass
import com.shlem666.jubro.ui.toolbars.BottomToolBarLayout
import com.shlem666.jubro.ui.toolbars.LeftToolBarLayout
import com.shlem666.jubro.ui.toolbars.RightToolBarLayout
import com.shlem666.jubro.ui.toolbars.TopToolBarLayout
import com.shlem666.jubro.feature.settings.SettingsDialog
import com.shlem666.jubro.feature.settings.AppSettings
import com.shlem666.jubro.feature.settings.SettingsUiState.Success

@Composable
fun JubroApp(
    viewModel: JubroViewModel = hiltViewModel(),
) {
    val appUiState by viewModel.appUiState.collectAsStateWithLifecycle()
    val isCompact = currentWindowAdaptiveInfo().windowSizeClass
        .windowWidthSizeClass == WindowWidthSizeClass.COMPACT
    var appSettings by rememberSaveable(stateSaver = AppSettings.Saver) {
        mutableStateOf( AppSettings() )
    }

    if (appUiState is Success) {
        appSettings = (appUiState as Success).appSettings
        if (!isCompact && appSettings.hideStatusBar) {
            HideStatusBar(true)
        } else {
            HideStatusBar(false)
        }
        LockScreenOrientation(appSettings.screenOrient)
    }

    var showSettingsDialog by rememberSaveable { mutableStateOf(false) }
    if (showSettingsDialog) {
        SettingsDialog( onDismiss = { showSettingsDialog = false } )
    }

    val bottomBar = @Composable { if (isCompact) BottomToolBarLayout() }
    val topBar = @Composable { if (isCompact) TopToolBarLayout(
        toggleSettingDialog = { showSettingsDialog = true }
    ) }
    val leftBar = @Composable { if (!isCompact) LeftToolBarLayout() }
    val rightBar = @Composable { if (!isCompact) RightToolBarLayout(
        toggleSettingDialog = { showSettingsDialog = true }
    ) }

    Scaffold(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background)
            .imePadding()
            .then(if (appSettings.notchPadding) {
                Modifier.displayCutoutPadding()
            } else { Modifier } )
        ,
        topBar = { topBar() },
        bottomBar = { bottomBar() },
    ) { innerPadding ->
        Row (
            Modifier.padding(innerPadding)
        ) {
            leftBar()
            Box( modifier = Modifier.weight(1f) ) {
                JubroWebView(
                    url = appSettings.jupyterUrl,
                    onUpdate = { webView -> viewModel.webView = webView },
                    onPageFinished = { viewModel.evalJS(
                        "JupyterLabOnPageFinished.js"
                    ) },
                )
            }
            rightBar()
        }
    }
}

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

@Composable
fun HideStatusBar(hide: Boolean) {
    val activity = LocalActivity.current
    val window = (activity as Activity).window
    val windowInsetsController =
        WindowCompat.getInsetsController(window, window.decorView)
    val statusBars = WindowInsetsCompat.Type.statusBars()
    if (hide) {
        windowInsetsController.hide(statusBars)
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat
                .BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    } else {
        windowInsetsController.show(statusBars)
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_DEFAULT
    }
}