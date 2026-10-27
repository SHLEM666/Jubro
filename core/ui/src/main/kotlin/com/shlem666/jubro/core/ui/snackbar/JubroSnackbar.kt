package com.shlem666.jubro.core.ui.snackbar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import com.shlem666.jubro.core.designsystem.theme.LocalSnackbarHostState

@Composable
fun JubroSnackbar(
    trigger: Boolean,
    hide: () -> Unit,
    message: String,
    actionLabel: String? = null,
    onAction: () -> Unit = { },
) {

    val snackbarHostState = LocalSnackbarHostState.current

    LaunchedEffect(trigger) {
        if (trigger) {
            if (
                showSnackbar(
                    hostState = snackbarHostState,
                    message = message,
                    action = actionLabel,
                )
            ) {
                onAction()
            }
            hide()
        }
    }

    LifecycleEventEffect(Lifecycle.Event.ON_STOP) {
        hide()
    }

    DisposableEffect(Unit) {
        onDispose {
            hide()
        }
    }
}