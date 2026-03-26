package com.shlem666.jubro

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import com.shlem666.jubro.core.designsystem.theme.JubroTheme
import com.shlem666.jubro.ui.JubroApp

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JubroTheme {
                JubroApp()
            }
        }
    }

    override fun getApplicationContext(): Context? {
        return super.getApplicationContext()
    }
}