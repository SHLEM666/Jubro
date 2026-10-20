package com.shlem666.jubro.core.ui.snackbar

import kotlinx.coroutines.flow.SharedFlow

interface SnackbarManager  {
    val messages: SharedFlow<String>
    fun showMessage(message: String)
}