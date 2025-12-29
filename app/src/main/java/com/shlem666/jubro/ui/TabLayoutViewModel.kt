/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.shlem666.jubro.ui

import android.util.Log
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import android.widget.Toast
import androidx.compose.foundation.pager.PagerState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shlem666.jubro.core.data.repository.UserDataRepository
import com.shlem666.jubro.ui.TabsUiState.Loading
import com.shlem666.jubro.ui.TabsUiState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class TabLayoutViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository,
    @ApplicationContext context: Context,
) : ViewModel() {

    val appContext = context

    val maxTabs = 10

    val pagerState = PagerState { maxTabs }

    val webViews: MutableList<WebView> = mutableListOf()

    val tabsUiState: StateFlow<TabsUiState> =
        userDataRepository.userData
            .map { userData ->
                if (userData.tabsUrls.isEmpty()) {
                    this.addTabsUrls(url = userData.jupyterUrl)
                    Loading
                } else {
                    if (webViews.isEmpty()) {
                        userData.tabsUrls.forEach { url ->
                            this.webViews.add(
                                newWebview(url)
                            )
                        }
                    }
                    Success(
                        tabLayoutResources = TabLayoutResources(
                            tabsUrls = userData.tabsUrls,
                            jupyterUrl = userData.jupyterUrl
                        ),
                    )
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = WhileSubscribed(5.seconds.inWholeMilliseconds),
                initialValue = Loading,
            )

    fun newTab() {
        if (tabsUiState.value is Success) {
            addTabsUrls(url = (tabsUiState.value as Success).tabLayoutResources.jupyterUrl)
        }
    }

    fun undo() {
        this.webViews[pagerState.currentPage]
            .evaluateJavascript(
                "document.execCommand('undo');",
                null,
            )
    }

    fun redo() {
        this.webViews[pagerState.currentPage]
            .evaluateJavascript(
                "document.execCommand('redo');",
                null,
            )
    }

    fun reloadCurrentPage() {
        webViews[pagerState.currentPage].reload()
        //Log.i("my", webViews[pagerState.currentPage].toString())
    }

    fun addTabsUrls(context: Context = appContext, url: String) {
        if (webViews.size < maxTabs) {
            webViews.add(newWebview(context = context, startUrl = url))
            viewModelScope.launch {
                userDataRepository.addTabsUrls(url)
                pagerState.scrollToPage(webViews.size - 1)
            }
        } else {
            Toast.makeText(appContext, "Max number of tabs is $maxTabs!", Toast.LENGTH_SHORT).show()
        }
    }

    fun editTabsUrls(newUrl: String, index: Int) {
        viewModelScope.launch {
            userDataRepository.editTabsUrls(newUrl, index)
        }
    }

    fun removeTabsUrls(index: Int,) {
        viewModelScope.launch {
            if (index < pagerState.currentPage) {
                pagerState.scrollToPage(pagerState.currentPage - 1)
            }
            userDataRepository.removeTabsUrls(index)
        }
        webViews.removeAt(index)
    }

    fun newWebview(
        startUrl: String = "http://localhost:8888/tree/jupyter/Test.ipynb",
        context: Context = appContext,
        viewModel: TabLayoutViewModel = this,
    ): WebView {
        return WebView(context).apply {
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
                                        document.body.appendChild(elem);
                                        document.addEventListener('contextmenu', (e) => {
                                            e.stopImmediatePropagation();
                                            e.stopPropagation();
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
                    val url = request.url.toString()
                    val currentTabIndex = viewModel.pagerState.currentPage
                    view.loadUrl(url)
                    viewModel.editTabsUrls(url, currentTabIndex)
                    return true
                }
            }
            loadUrl(startUrl)
            webChromeClient = WebChromeClient()
        }
    }
}

/**
 * Represents the set of Tabs URLs.
 */
data class TabLayoutResources(
    val tabsUrls: MutableList<String>,
    val jupyterUrl: String,
)

sealed interface TabsUiState {
    data object Loading : TabsUiState
    data class Success(val tabLayoutResources: TabLayoutResources) : TabsUiState
}
