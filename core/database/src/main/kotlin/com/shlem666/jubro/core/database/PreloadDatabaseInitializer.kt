package com.shlem666.jubro.core.database

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import javax.inject.Inject
import com.shlem666.jubro.core.common.Dispatcher
import com.shlem666.jubro.core.common.JubroDispatchers.IO
import com.shlem666.jubro.core.database.dao.RecentTextFieldValueDao
import com.shlem666.jubro.core.database.model.RecentTextFieldValueEntity

class PreloadDatabaseInitializer @Inject constructor(
    private val recentTextFieldValueDao: RecentTextFieldValueDao,
    @param:Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
) : DatabaseInitializer {

    override suspend fun initialize() {
        withContext(ioDispatcher) {
            with(recentTextFieldValueDao::insertOrReplaceRecentTextFieldValue) {
                invoke(
                    RecentTextFieldValueEntity(
                        fieldName = "jupyterUrl",
                        value = "https://jupyter.org/try-jupyter/lab/",
                        lastUseDate = Clock.System.now(),
                    )
                )
                invoke(
                    RecentTextFieldValueEntity(
                        fieldName = "jupyterUrl",
                        value = "http://localhost:8888/lab/",
                        lastUseDate = Clock.System.now(),
                    )
                )
            }
        }
    }
}