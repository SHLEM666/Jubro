package com.shlem666.jubro.ui

import android.view.KeyEvent
import android.webkit.WebView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import com.shlem666.jubro.core.data.repository.CodeDataRepository
import com.shlem666.jubro.core.data.repository.UserDataRepository
import com.shlem666.jubro.ui.webview.WebViewController

@HiltViewModel
class UiViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository,
    private val codeDataRepository: CodeDataRepository,
    private val webViewController: WebViewController,
) : ViewModel() {

    private var useJsApi = false

    init {
        viewModelScope.launch {
            userDataRepository.userData.collect {
                useJsApi = it.useJsApi
            }
        }
    }

    private fun getScript(fileName: String): String {
        return codeDataRepository.files[fileName] ?: ""
    }

    fun attacheWebView(webView: WebView) {
        webViewController.attacheWebView(webView)
    }

    fun onPageFinished() {
        webViewController.evalJS(
            getScript("JupyterLabOnPageFinished.js")
        )
    }

    fun reload() {
        webViewController.reload()
    }
    fun run() {
        webViewController.simulateKeyPress(
            code = KeyEvent.KEYCODE_ENTER,
            metaStateCode1 = KeyEvent.META_SHIFT_ON
        )
    }

    fun toggleLeftSideBar() {
        if (useJsApi) {
            webViewController.evalJS(
                getScript("ToggleLeftSideBar.js")
            )
        } else {
            webViewController.simulateKeyPress(
                code = KeyEvent.KEYCODE_B,
                metaStateCode1 = KeyEvent.META_CTRL_ON
            )
            webViewController.simulateKeyPress(
                code = KeyEvent.KEYCODE_Y,
                metaStateCode1 = KeyEvent.META_SHIFT_ON,
                metaStateCode2 = KeyEvent.META_CTRL_ON
            )
        }
    }
    fun toggleRightSideBar() {
        if (useJsApi) {
            webViewController.evalJS(
                getScript("ToggleRightSideBar.js")
            )
        } else {
            webViewController.simulateKeyPress(
                code = KeyEvent.KEYCODE_J,
                metaStateCode1 = KeyEvent.META_CTRL_ON
            )
            webViewController.simulateKeyPress(
                code = KeyEvent.KEYCODE_O,
                metaStateCode1 = KeyEvent.META_SHIFT_ON,
                metaStateCode2 = KeyEvent.META_CTRL_ON
            )
        }
    }

    fun undo() {
        if (useJsApi) {
            webViewController.evalJS(
                getScript("Undo.js")
            )
        } else {
            webViewController.simulateKeyPress(
                code = KeyEvent.KEYCODE_Z,
                metaStateCode1 = KeyEvent.META_CTRL_ON
            )
        }
    }
    fun redo() {
        if (useJsApi) {
            webViewController.evalJS(
                getScript("Redo.js")
            )
        } else {
            webViewController.simulateKeyPress(
                code = KeyEvent.KEYCODE_Z,
                metaStateCode1 = KeyEvent.META_SHIFT_ON,
                metaStateCode2 = KeyEvent.META_CTRL_ON
            )
        }
    }
    fun tab() {
        webViewController.simulateKeyPress(KeyEvent.KEYCODE_TAB)
    }

    fun arrowUp() {
        webViewController.simulateKeyPress(KeyEvent.KEYCODE_DPAD_UP)
    }
    fun arrowDown() {
        webViewController.simulateKeyPress(KeyEvent.KEYCODE_DPAD_DOWN)
    }
    fun arrowLeft() {
        webViewController.simulateKeyPress(KeyEvent.KEYCODE_DPAD_LEFT)
    }
    fun arrowRight() {
        webViewController.simulateKeyPress(KeyEvent.KEYCODE_DPAD_RIGHT)
    }
}