package com.shlem666.jubro.core.ui.snackbar

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

interface SnackbarManager  {
    val messages: SharedFlow<String>
    fun showMessage(message: String)
}

@Singleton
class JubroSnackbarManager @Inject constructor() : SnackbarManager {
    private val _messages = MutableSharedFlow<String>(extraBufferCapacity = 1)
    override val messages: SharedFlow<String> = _messages.asSharedFlow()

    override fun showMessage(message: String) {
        _messages.tryEmit(message)
    }
}