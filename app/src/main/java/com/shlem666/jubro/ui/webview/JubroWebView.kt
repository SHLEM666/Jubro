package com.shlem666.jubro.ui.webview

import android.annotation.SuppressLint
import android.content.Intent
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun JubroWebView(
    startUrl: String,
    onPageFinished: () -> Unit = { },
    onUpdate: (webView: WebView) -> Unit = { },
) {
    // so that the jupyterUrl value in the shouldOverrideUrlLoading
    // function is not taken from the closure
    val url by rememberUpdatedState(startUrl)

    // webview needs exactly Activity Context (LocalContext.current)
    // to mobile version of select html-element may work properly
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                settings.apply {
                    @SuppressLint("SetJavaScriptEnabled")
                    javaScriptEnabled = true
                    domStorageEnabled = true
                }
                layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                )
                webViewClient = object : WebViewClient() {
                    override fun onPageFinished(
                        view: WebView?,
                        url: String?
                    ) {
                        super.onPageFinished(view, url)
                        onPageFinished()
                    }
                    override fun shouldOverrideUrlLoading(
                        view: WebView,
                        request: WebResourceRequest
                    ): Boolean {
                        val requestUrl = request.url.toString()
                        if (
                            "$requestUrl/".contains(
                                url, true
                            )
                        ) {
                            view.loadUrl(requestUrl)
                        } else {
                            view.context.startActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    request.url
                                )
                            )
                        }
                        return true
                    }
                }
                webChromeClient = WebChromeClient()
            }
        },
        update = { webView ->
            webView.loadUrl(url)
            onUpdate(webView)
        },
        onReset = { webView ->
            webView.stopLoading()
            webView.loadUrl("about:blank")
            webView.clearHistory()
        },
    )
}