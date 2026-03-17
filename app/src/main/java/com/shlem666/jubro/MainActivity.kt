package com.shlem666.jubro

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.shlem666.jubro.core.designsystem.theme.JubroTheme
import com.shlem666.jubro.ui.JubroApp
import com.shlem666.jubro.ui.webview.WebViewController
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var webViewController: WebViewController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JubroTheme {
                JubroApp(
                    webViewController = webViewController
                )
            }
        }
    }

    override fun getApplicationContext(): Context? {
        return super.getApplicationContext()
    }
}