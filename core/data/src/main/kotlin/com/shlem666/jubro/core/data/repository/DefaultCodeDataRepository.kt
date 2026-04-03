package com.shlem666.jubro.core.data.repository

import com.shlem666.jubro.core.data.CodeDataSource
import javax.inject.Inject

class DefaultCodeDataRepository @Inject constructor(
    private val codeDataSource: CodeDataSource
) : CodeDataRepository {
    override val files: Map<String, String> = codeDataSource.getCodeData()
}