package com.shlem666.jubro.util

import android.content.Context
import android.content.res.AssetManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class JsManager @Inject constructor(
    @param:ApplicationContext private val context: Context,
) {
    private val assetManager: AssetManager = context.assets
    private val path = "js"
    private val fileNames = assetManager.list(path)

    val scripts = fileNames?.associateWith { fileName ->
        assetManager
            .open("$path/$fileName")
            .bufferedReader().use { it.readText() }
    }
}