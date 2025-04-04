package org.adastra.curriculum

import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.russhwolf.settings.SharedPreferencesSettings
import org.adastra.curriculum.auth.TokenManager
import org.adastra.curriculum.client.BackendApi

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val baseUrl = BuildConfig.BASE_URL
        val sharedPreferences: SharedPreferences = getSharedPreferences("curriculum_preferences",
            MODE_PRIVATE
        )

        val settings = SharedPreferencesSettings(sharedPreferences)
        val tokenManager = TokenManager(settings)
        tokenManager.clearToken()
        val backendApi = BackendApi(baseUrl, tokenManager)
        setContent {
            App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}