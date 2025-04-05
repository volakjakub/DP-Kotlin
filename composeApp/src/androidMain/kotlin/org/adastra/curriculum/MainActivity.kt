package org.adastra.curriculum

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import org.adastra.curriculum.auth.LoginScreen
import org.adastra.curriculum.auth.LoginViewModel

class MainActivity : ComponentActivity() {
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        loginViewModel.logout()
        // Observe the token validity LiveData
        loginViewModel.isLoggedIn().observe(this, Observer { isLoggedIn ->
            if (isLoggedIn) {
                // If the token is valid, show the main content
                showMainContent()
            } else {
                // If the token is invalid, redirect to the login screen
                redirectToLogin()
            }
        })

        // Check the token status when the app starts
        loginViewModel.checkToken()
    }

    private fun showMainContent() {
        // Display main content view (your app's main UI)
        setContent {
            App(loginViewModel)
        }
    }

    private fun redirectToLogin() {
        // Redirect to the login screen if not logged in
        setContent {
            LoginScreen(loginViewModel)
        }
    }
}