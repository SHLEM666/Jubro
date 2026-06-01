package com.shlem666.jubro.ui.webview

import android.os.SystemClock
import android.view.InputDevice
import android.view.KeyEvent
import android.webkit.WebView
import javax.inject.Inject
import com.shlem666.jubro.core.data.repository.CodeDataRepository

class WebViewController @Inject constructor(
    private val codeDataRepository: CodeDataRepository
) {
    lateinit var webView: WebView

    fun attacheWebView(webView: WebView) {
        this.webView = webView
    }

    fun reload() {
        webView.reload()
    }

    fun evalJS(fileName: String) {
        webView.evaluateJavascript(
            codeDataRepository.files[fileName] ?: "",
            null
        )
    }

    fun simulateKeyPress(
        code: Int,
        metaStateCode1: Int = 0,
        metaStateCode2: Int = 0
    ) {
        val eventTime = SystemClock.uptimeMillis()
        webView.dispatchKeyEvent(
            KeyEvent(
                eventTime,
                eventTime,
                KeyEvent.ACTION_DOWN,
                code,
                0,
                metaStateCode1 or metaStateCode2,
                0,
                0,
                0,
                InputDevice.SOURCE_KEYBOARD
            )
        )
        webView.dispatchKeyEvent(
            KeyEvent(
                eventTime + 50,
                eventTime + 50,
                KeyEvent.ACTION_UP,
                code,
                0,
                metaStateCode1 or metaStateCode2,
                0,
                0,
                0,
                InputDevice.SOURCE_KEYBOARD
            )
        )
    }
}