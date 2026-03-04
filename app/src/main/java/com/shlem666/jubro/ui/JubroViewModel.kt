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

import android.annotation.SuppressLint
import android.content.Context
import android.view.KeyEvent
import android.webkit.WebView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds
import com.shlem666.jubro.core.data.repository.UserDataRepository
import com.shlem666.jubro.feature.settings.AppSettings
import com.shlem666.jubro.feature.settings.SettingsUiState
import com.shlem666.jubro.feature.settings.SettingsUiState.Loading
import com.shlem666.jubro.feature.settings.SettingsUiState.Success
import com.shlem666.jubro.util.JsManager

@HiltViewModel
class JubroViewModel @Inject constructor(
    userDataRepository: UserDataRepository,
    private val jsManager: JsManager,
    @param:ApplicationContext private val context: Context,
) : ViewModel() {

    @SuppressLint("StaticFieldLeak")
    var webView = WebView(context)

    val appUiState: StateFlow<SettingsUiState> =
        userDataRepository.userData
            .map { userData ->
                if (userData.jupyterUrl.isEmpty()) {
                    Loading
                } else {
                    Success(
                        appSettings = AppSettings(
                            jupyterUrl = userData.jupyterUrl,
                            notchPadding =  userData.notchPadding,
                            hideStatusBar = userData.hideStatusBar,
                            screenOrient = userData.screenOrient,
                        ),
                    )
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = WhileSubscribed(5.seconds.inWholeMilliseconds),
                initialValue = Loading,
            )

    fun evalJS(scriptName: String) {
        webView.evaluateJavascript(
            jsManager.scripts?.get(scriptName) ?: "",
            null
        )
    }

    fun simulateKeyPress(code: Int) {
        webView.dispatchKeyEvent( KeyEvent(KeyEvent.ACTION_DOWN, code) )
        webView.dispatchKeyEvent( KeyEvent(KeyEvent.ACTION_UP, code) )
    }
}
