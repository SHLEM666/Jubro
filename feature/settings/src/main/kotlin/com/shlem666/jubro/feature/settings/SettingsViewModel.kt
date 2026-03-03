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

package com.shlem666.jubro.feature.settings

import androidx.compose.runtime.saveable.Saver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds
import com.shlem666.jubro.core.data.repository.UserDataRepository
import com.shlem666.jubro.feature.settings.UiState.Loading
import com.shlem666.jubro.feature.settings.UiState.Success

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository,
) : ViewModel() {

    val uiState: StateFlow<UiState> =
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

    fun applySettings(settings: AppSettings) {
        viewModelScope.launch {
            userDataRepository.setJupyterUrl(settings.jupyterUrl)
            userDataRepository.setNotchPadding(settings.notchPadding)
            userDataRepository.setHideStatusBar(settings.hideStatusBar)
            userDataRepository.setScreenOrient(settings.screenOrient)
        }
    }
}

/**
 * Represents the settings which the user can edit within the app.
 */
data class AppSettings(
    val jupyterUrl: String,
    val notchPadding: Boolean,
    val hideStatusBar: Boolean,
    val screenOrient: Int,
) {
    companion object {
        val Saver = Saver< AppSettings, List<Any> >(
            save = { listOf(
                it.jupyterUrl,
                it.notchPadding,
                it.hideStatusBar,
                it.screenOrient,
            ) },
            restore = { AppSettings(
                jupyterUrl = it[0] as String,
                notchPadding = it[1] as Boolean,
                hideStatusBar = it[2] as Boolean,
                screenOrient = it[3] as Int,
            ) },
        )
    }
}

sealed interface UiState {
    data object Loading : UiState
    data class Success(val appSettings: AppSettings) : UiState
}
