package com.shlem666.jubro.core.ui.snackbar

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

class SnackbarViewModel @Inject constructor() : ViewModel() {
    private val _messages = MutableSharedFlow<SnackbarEvent?>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )
    val messages: SharedFlow<SnackbarEvent?> = _messages.asSharedFlow()

    fun showMessage(event: SnackbarEvent) {
        _messages.tryEmit(event)
    }

    fun dismiss() {
        _messages.tryEmit(null)
    }
}

data class SnackbarEvent(
    val message: String,
    val actionLabel: String? = null,
    val onAction: (() -> Unit)? = null
)