package com.shlem666.jubro.core.data.repository

import com.shlem666.jubro.core.data.DefaultJsDataSource
import javax.inject.Inject

class DefaultJsDataRepository @Inject constructor(
    private val defaultJsDataSource: DefaultJsDataSource
) : JsDataRepository {
    override val scripts: Map<String, String> = defaultJsDataSource.getJsData()
}