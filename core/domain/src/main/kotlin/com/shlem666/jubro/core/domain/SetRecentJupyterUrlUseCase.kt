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

package com.shlem666.jubro.core.domain

import com.shlem666.jubro.core.data.repository.RecentTextRepository
import javax.inject.Inject

/**
 * A use case which inserts or updates the recent Jupyter URL field values to database.
 */
class SetRecentJupyterUrlUseCase @Inject constructor(
    private val recentTextRepository: RecentTextRepository
) {
    suspend operator fun invoke(
        value: String,
        fieldName: String = "jupyterUrl",
    ) {
        recentTextRepository.insertOrReplaceRecentTextFieldValue(
            recentTextFieldValue = value,
            fieldName = fieldName,
        )
    }
}
