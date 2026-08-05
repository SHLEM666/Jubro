package com.shlem666.jubro.core.database

import androidx.sqlite.db.SupportSQLiteDatabase

interface DatabaseInitializer {
    suspend fun initialize(db: SupportSQLiteDatabase)
}