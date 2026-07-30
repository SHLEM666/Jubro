/*
 * Copyright 2023 The Android Open Source Project
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

import com.shlem666.jubro.core.data.model.RecentTextFieldValue
import kotlinx.coroutines.flow.Flow

/**
 * Data layer interface for the recent text fields values.
 */
interface RecentTextRepository {

    /**
     * Get the recent values for [fieldNames] up to the number of values specified as [limit].
     */
    fun getRecentTextFieldValues(
        fieldNames: List<String>,
        limit: Int,
    ): Flow< List<RecentTextFieldValue> >

    /**
     * Insert or replace the [recentTextFieldValue] as part of the recent values of [fieldName].
     */
    suspend fun insertOrReplaceRecentTextFieldValue(
        fieldName: String,
        recentTextFieldValue: String,
    )

    /**
     * Clear the [recentTextFieldValue] for [fieldName].
     */
    suspend fun clearRecentTextFieldValues(
        fieldName: String,
        recentTextFieldValue: String,
    )
}
