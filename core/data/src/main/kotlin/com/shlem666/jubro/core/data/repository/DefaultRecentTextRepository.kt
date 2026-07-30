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
import com.shlem666.jubro.core.data.model.asExternalModel
import com.shlem666.jubro.core.database.model.RecentTextFieldValueEntity
import com.shlem666.jubro.core.database.dao.RecentTextFieldValueDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import javax.inject.Inject

internal class DefaultRecentTextRepository @Inject constructor(
    private val recentTextFieldValueDao: RecentTextFieldValueDao,
) : RecentTextRepository {
    override suspend fun insertOrReplaceRecentTextFieldValue(
        fieldName: String,
        recentTextFieldValue: String,
    ) {
        recentTextFieldValueDao.insertOrReplaceRecentTextFieldValue(
            RecentTextFieldValueEntity(
                value = recentTextFieldValue,
                fieldName = fieldName,
                lastUseDate = Clock.System.now(),
            ),
        )
    }

    override fun getRecentTextFieldValues(
        fieldNames: List<String>,
        limit: Int,
    ): Flow< List<RecentTextFieldValue> > =
        recentTextFieldValueDao.getRecentTextFieldValueEntities(
            fieldNames,
            limit,
        )
        .map { textFieldValue ->
            textFieldValue.map { it.asExternalModel() }
        }

    override suspend fun clearRecentTextFieldValues(
        fieldName: String,
        recentTextFieldValue: String,
    ) {
        recentTextFieldValueDao.clearRecentTextFieldValues(
            fieldName,
            recentTextFieldValue,
        )
    }
}
