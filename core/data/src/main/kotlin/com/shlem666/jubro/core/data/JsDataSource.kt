package com.shlem666.jubro.core.data

import android.content.Context
import android.content.res.AssetManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class JsDataSource @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    private val assetManager: AssetManager = context.assets
    private val path = "js"
    private val fileNames = assetManager.list(path)

    fun getJsData() = fileNames?.associateWith { fileName ->
        assetManager
            .open("$path/$fileName")
            .bufferedReader().use { it.readText() }
    } ?: mapOf()
}