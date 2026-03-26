package com.shlem666.jubro.ui.webview

import android.annotation.SuppressLint
import android.content.Intent
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun JubroWebView(
    url: String,
    viewModel: JubroWebViewViewModel = hiltViewModel(),
) {
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
                        viewModel.onPageFinished()
                    }
                    override fun shouldOverrideUrlLoading(
                        view: WebView,
                        request: WebResourceRequest
                    ): Boolean {
                        view.context.startActivity(Intent(
                            Intent.ACTION_VIEW,
                            request.url
                        ) )
                        return true
                    }
                }
                webChromeClient = WebChromeClient()
            }
        },
        update = { webView ->
            webView.loadUrl(url)
            viewModel.attacheWebView(webView)
        },
        onReset = { webView ->
            webView.stopLoading()
            webView.loadUrl("about:blank")
            webView.clearHistory()
        },
    )
}