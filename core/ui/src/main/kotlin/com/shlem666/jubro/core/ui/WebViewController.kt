package com.shlem666.jubro.core.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext

/**
 * Interface for managing WebViews within a larger application structure.
 * This interface provides methods to retrieve and dispose of WebViews,
 * allowing for centralized control and management of their lifecycle.
 */
interface WebViewController {
    fun retrieve(key: Int): WebView
    fun dispose(key: Int, isPop: Boolean)
}

/**
 * Default implementation of [WebViewController] that manages a collection of [WebView] instances.
 *
 * This class provides methods for retrieving and disposing of [WebView] instances associated with unique keys.
 * It maintains a back stack of WebViews, allowing for reuse and management of multiple web browsing contexts.
 *
 * @property context The application context used for creating new WebViews.
 * @property backStack A mutable map storing [WebView] instances keyed by unique identifiers (String).
 *                     Defaults to an empty hash map.
 */
class DefaultWebViewController(
    private val context: Context,
    private val backStack: HashMap<Int, WebView> = hashMapOf()
) : WebViewController {

    val startUrl = "http://localhost:8888/tree/jupyter/Test.ipynb"

    override fun retrieve(
        key: Int,
    ): WebView {
        return backStack[key] ?: run {
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
                    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                        super.onPageStarted(view, url, favicon)
                    }
                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        val js: String? = """
                            (function (window) {
                                if (/.ipynb/.test(window.location.href)) {
                                    try {
                                        var elem = document.createElement("style");
                                        elem.innerHTML += "#jp-MainMenu {overflow-x: scroll;}";
                                        elem.innerHTML += ".jp-NotebookTrustedStatus {display: none;}";
                                        //elem.innerHTML += "select {display: none;}";
                                        document.body.appendChild(elem);
                                        document.addEventListener('contextmenu', (e) => {
                                            if (e.target.closest('.jp-CodeMirrorEditor')) {
                                                e.stopImmediatePropagation();
                                                e.stopPropagation();
                                            }
                                        }, true);
                                    } catch (err) {
                                        alert(err);
                                    };
                                };
                            })(window);
                        """
                        view?.evaluateJavascript("javascript:$js", null)
                    }
                    override fun shouldOverrideUrlLoading(
                        view: WebView,
                        request: WebResourceRequest
                    ): Boolean {
                        view.loadUrl(request.url.toString())
                        return true
                    }
                }
                loadUrl(startUrl)
                webChromeClient = WebChromeClient()
                backStack[key] = this
            }
        }
    }

    override fun dispose(
        key: Int,
        isPop: Boolean
    ) {
        if (isPop) {
            val removed = backStack.remove(key)
            removed?.destroy()
        }
    }

    companion object {
        private var savedBackStack: HashMap<Int, WebView>? = null
        fun Saver(context: Context) = listSaver(
            save = {
                savedBackStack = it.backStack
                listOf("")
            },
            restore = {
                DefaultWebViewController(
                    context = context,
                    backStack = savedBackStack?: hashMapOf(),
                ).apply { savedBackStack = null }
            },
        )
    }
}

/**
 * Creates and remembers a [WebViewController] instance across recompositions.
 *
 * This function provides a way to manage the state of a [WebViewController] within a
 * Compose UI. It leverages [rememberSaveable] to preserve the state across configuration
 * changes (e.g., screen rotation) and process death.
 *
 * The [WebViewController] is created using the provided [Context] from [LocalContext].
 *
 * Example Usage:
 * ```
 * @Composable
 * fun MyWebViewScreen() {
 *     val webViewController = rememberWebViewController()
 *
 *     // Use the webViewController with a WebView composable
 *     WebView(
 *         controller = webViewController,
 *         modifier = Modifier.fillMaxSize()
 *     )
 * }
 * ```
 *
 * @return A [WebViewController] instance that is remembered across recompositions.
 */
@Composable
fun rememberWebViewController(): WebViewController {
    val context = LocalContext.current
    return rememberSaveable(
        saver = DefaultWebViewController.Saver(context)
    ) {
        DefaultWebViewController(context)
    }
}
