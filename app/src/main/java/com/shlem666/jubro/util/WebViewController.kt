package com.shlem666.jubro.util

import android.view.KeyEvent
import android.webkit.WebView
import javax.inject.Inject
import com.shlem666.jubro.core.data.repository.JsDataRepository

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
        webView.dispatchKeyEvent( KeyEvent(KeyEvent.ACTION_DOWN, code) )
        webView.dispatchKeyEvent( KeyEvent(KeyEvent.ACTION_UP, code) )
    }
}
