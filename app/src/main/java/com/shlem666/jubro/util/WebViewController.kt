package com.shlem666.jubro.util

import android.content.Context
import android.content.res.AssetManager
import android.view.KeyEvent
import android.webkit.WebView
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class WebViewController @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    lateinit var webView: WebView

    private val assetManager: AssetManager = context.assets
    private val path = "js"
    private val fileNames = assetManager.list(path)
    private val scripts = fileNames?.associateWith { fileName ->
        assetManager
            .open("$path/$fileName")
            .bufferedReader().use { it.readText() }
    }

    fun evalJS(scriptName: String) {
        webView.evaluateJavascript(
            scripts?.get(scriptName) ?: "",
            null
        )
    }

    fun simulateKeyPress(code: Int) {
        webView.dispatchKeyEvent( KeyEvent(KeyEvent.ACTION_DOWN, code) )
        webView.dispatchKeyEvent( KeyEvent(KeyEvent.ACTION_UP, code) )
    }
}
