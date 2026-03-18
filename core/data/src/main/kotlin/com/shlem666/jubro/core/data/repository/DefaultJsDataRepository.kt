package com.shlem666.jubro.core.data.repository

import com.shlem666.jubro.core.data.JsDataSource
import javax.inject.Inject

class DefaultJsDataRepository @Inject constructor(
    private val jsDataSource: JsDataSource
) : JsDataRepository {
    override val scripts: Map<String, String> = jsDataSource.getJsData()
}