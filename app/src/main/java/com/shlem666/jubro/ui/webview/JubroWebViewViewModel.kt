package com.shlem666.jubro.ui.webview

import android.view.KeyEvent
import android.webkit.WebView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.shlem666.jubro.core.data.repository.UserDataRepository

@HiltViewModel
class JubroWebViewViewModel @Inject constructor(
    userDataRepository: UserDataRepository,
    private val webViewController: WebViewController
) : ViewModel() {

    private var useJsApi = false

    init {
        viewModelScope.launch {
            userDataRepository.userData.collect {
                useJsApi = it.useJsApi
            }
        }
    }

    fun undo() {
        if (useJsApi) {
            webViewController.evalJS("Undo.js")
        } else {
            webViewController.simulateKeyPress(
                code = KeyEvent.KEYCODE_Z,
                metaStateCode1 = KeyEvent.META_CTRL_ON
            )
        }
    }
    fun redo() {
        if (useJsApi) {
            webViewController.evalJS("Redo.js")
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
    fun run() {
        webViewController.simulateKeyPress(
            code = KeyEvent.KEYCODE_ENTER,
            metaStateCode1 = KeyEvent.META_SHIFT_ON
        )
    }

    fun toggleLeftSideBar() {
        if (useJsApi) {
            webViewController.evalJS("ToggleLeftSideBar.js")
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
            webViewController.evalJS("ToggleRightSideBar.js")
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

    fun reload() {
        webViewController.webView.reload()
    }

    fun arrowUp() {
        webViewController.simulateKeyPress(KeyEvent.KEYCODE_DPAD_UP)
    }
    fun arrowLeft() {
        webViewController.simulateKeyPress(KeyEvent.KEYCODE_DPAD_LEFT)
    }
    fun arrowRight() {
        webViewController.simulateKeyPress(KeyEvent.KEYCODE_DPAD_RIGHT)
    }
    fun arrowDown() {
        webViewController.simulateKeyPress(KeyEvent.KEYCODE_DPAD_DOWN)
    }

    fun attacheWebView(webView: WebView) {
        webViewController.webView = webView
    }

    fun onPageFinished() {
        webViewController.evalJS(
            "JupyterLabOnPageFinished.js"
        )
    }
}