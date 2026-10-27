package com.shlem666.jubro.core.ui.snackbar

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult

suspend fun showSnackbar(
    hostState: SnackbarHostState,
    message: String,
    action: String? = null,
    duration: SnackbarDuration = SnackbarDuration.Short,
) : Boolean {
    return hostState.showSnackbar(
        message = message,
        actionLabel = action,
        duration = duration,
    ) == SnackbarResult.ActionPerformed
}