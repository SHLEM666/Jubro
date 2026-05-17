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

package com.shlem666.jubro.core.data.repository

import androidx.annotation.VisibleForTesting
import com.shlem666.jubro.core.datastore.JubroPreferencesDataSource
import com.shlem666.jubro.core.model.data.UserData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class OfflineFirstUserDataRepository @Inject constructor(
    private val jubroPreferencesDataSource: JubroPreferencesDataSource,
) : UserDataRepository {

    override val userData: Flow<UserData> =
        jubroPreferencesDataSource.userData

    //@VisibleForTesting
    override suspend fun setJupyterUrl(jupyterUrl: String) {
        jubroPreferencesDataSource.setJupyterUrl(jupyterUrl)
    }

    override suspend fun setNotchPadding(notchPadding: Boolean) {
        jubroPreferencesDataSource.setNotchPadding(notchPadding)
    }

    override suspend fun setHideStatusBar(hideStatusBar: Boolean) {
        jubroPreferencesDataSource.setHideStatusBar(hideStatusBar)
    }

    override suspend fun setScreenOrient(screenOrient: Int) {
        jubroPreferencesDataSource.setScreenOrient(screenOrient)
    }

    override suspend fun setUseJsApi(useJsApi: Boolean) {
        jubroPreferencesDataSource.setUseJsApi(useJsApi)
    }

    override suspend fun setDarkTheme(darkTheme: Boolean) {
        jubroPreferencesDataSource.setDarkTheme(darkTheme)
    }
}
