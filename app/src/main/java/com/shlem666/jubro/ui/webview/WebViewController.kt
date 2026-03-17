package com.shlem666.jubro.ui.webview

import android.view.KeyEvent
import android.webkit.WebView
import com.shlem666.jubro.core.data.repository.JsDataRepository
import javax.inject.Inject

class WebViewController @Inject constructor(
    private val jsDataRepository: JsDataRepository
) {
    lateinit var webView: WebView

    fun evalJS(scriptName: String) {
        webView.evaluateJavascript(
            jsDataRepository.scripts[scriptName] ?: "",
            null
        )
    }

    fun simulateKeyPress(code: Int) {
        webView.dispatchKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, code))
        webView.dispatchKeyEvent(KeyEvent(KeyEvent.ACTION_UP, code))
    }
}