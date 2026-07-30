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

package com.shlem666.jubro.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.shlem666.jubro.core.database.model.RecentTextFieldValueEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO for [RecentTextFieldValueEntity] access
 */
@Dao
interface RecentTextFieldValueDao {
    @Query(value = "SELECT * FROM recentTextFieldValues WHERE fieldName in (:fieldNames) ORDER BY lastUseDate DESC LIMIT :limit")
    fun getRecentTextFieldValueEntities(
        fieldNames: List<String>,
        limit: Int,
    ): Flow<List<RecentTextFieldValueEntity>>

    @Upsert
    suspend fun insertOrReplaceRecentTextFieldValue(
        recentTextFieldValue: RecentTextFieldValueEntity
    )

    @Query(value = "DELETE FROM recentTextFieldValues WHERE value = :value AND fieldName = :fieldName")
    suspend fun clearRecentTextFieldValues(
        fieldName: String,
        value: String,
    )
}
