package com.shlem666.jubro.ui.webview

import android.webkit.JavascriptInterface

class JubroJsInterface(
    private val setUseJsApi: (value: Boolean) -> Unit,
) {
    @JavascriptInterface
    fun jupyterAppDetected(value: Boolean) {
        setUseJsApi(value)
    }
}