package com.shlem666.jubro.core.ui.snackbar

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect

@Composable
fun JubroSnackbar(
    hostState: SnackbarHostState,
    trigger: Boolean,
    hideSnackbar: () -> Unit,
    message: String,
    actionLabel: String? = null,
    onAction: () -> Unit = { },
) {
    LaunchedEffect(trigger) {
        if (trigger) {
            if (
                hostState.showSnackbar(
                    message = message,
                    actionLabel = actionLabel,
                    duration = SnackbarDuration.Short,
                ) == SnackbarResult.ActionPerformed
            ) {
                onAction()
            }
            hideSnackbar()
        }
    }

    LifecycleEventEffect(Lifecycle.Event.ON_STOP) {
        hideSnackbar()
    }

    DisposableEffect(Unit) {
        onDispose {
            hideSnackbar()
        }
    }
}