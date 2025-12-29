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

package com.shlem666.jubro.core.datastore

import android.util.Log
import androidx.datastore.core.DataStore
import com.shlem666.jubro.core.model.data.UserData
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class JubroPreferencesDataSource @Inject constructor(
    private val userPreferences: DataStore<UserPreferences>,
) {
    val userData = userPreferences.data
        .map {
            UserData(
                jupyterUrl = it.jupyterUrl,
                // !!! It needs to add corresponding suffix ("Map", "List", etc.) !!!
                tabsUrls = it.tabsUrlsList,
            )
        }

    suspend fun setJupyterUrl(jupyterUrl: String) {
        try {
            userPreferences.updateData {
                it.copy {
                    this.jupyterUrl = jupyterUrl
                }
            }
        } catch (ioException: IOException) {
            Log.e("JubroPreferences", "Failed to update user preferences", ioException)
        }
    }

    suspend fun addTabsUrls(url: String) {
        try {
            userPreferences.updateData {
                it.copy {
                    tabsUrls.add(url)
                }
            }
        } catch (ioException: IOException) {
            Log.e("JubroPreferences", "Failed to update user preferences", ioException)
        }
    }

    suspend fun editTabsUrls(url: String, index: Int) {
        try {
            userPreferences.updateData {
                it.copy {
                    val newTabsUrls = tabsUrls.toMutableList()
                    newTabsUrls[index] = url
                    tabsUrls.clear()
                    tabsUrls.addAll(newTabsUrls)
                }
            }
        } catch (ioException: IOException) {
            Log.e("JubroPreferences", "Failed to update user preferences", ioException)
        }
    }

    suspend fun removeTabUrl(index : Int) {
        try {
            userPreferences.updateData {
                it.copy {
                    val newTabsUrls = tabsUrls.filterIndexed { i, it ->
                        i != index
                    }
                    tabsUrls.clear()
                    tabsUrls.addAll(newTabsUrls)
                }
            }
        } catch (ioException: IOException) {
            Log.e("JubroPreferences", "Failed to update user preferences", ioException)
        }
    }

}
