package com.shlem666.jubro.ui.webview

import android.webkit.JavascriptInterface
import com.shlem666.jubro.ui.UiViewModel
import javax.inject.Inject

class JubroJsInterface @Inject constructor(
    private val viewModel: UiViewModel
) {
    @JavascriptInterface
    fun setUseJsApi(value: Boolean) {
        viewModel.setUseJsApi(value)
    }
}