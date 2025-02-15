package com.openclassrooms.eventorias

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.openclassrooms.eventorias.screen.LogInProvidersScreen
import com.openclassrooms.eventorias.screen.Screen
import com.openclassrooms.eventorias.ui.theme.EventoriasTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            EventoriasTheme {
                EventoriasNavHost(navController)

            }
        }
    }

    @Composable
    fun EventoriasNavHost(navHostController: NavHostController) {
        NavHost(
            navController = navHostController, startDestination = Screen.Auth.route
        ) {
            navigation(
                startDestination = Screen.LogInProviders.route,
                route = Screen.Auth.route
            ) {
                composable(Screen.LogInProviders.route) {
                    LogInProvidersScreen()
                }
                composable(Screen.LogInWithMail.route) {
                }
                composable(Screen.LogInWithPassword.route) {
                }
                composable(Screen.CreateAccountWithMail.route) {
                }
                composable(Screen.RecoverAccountWithMail.route) {
                }
            }
        }
    }
}
