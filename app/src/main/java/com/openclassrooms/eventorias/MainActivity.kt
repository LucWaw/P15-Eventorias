package com.openclassrooms.eventorias

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.openclassrooms.eventorias.screen.createaccountwithmail.CreateAccountWithMailScreen
import com.openclassrooms.eventorias.screen.LoginMailSelectorScreen
import com.openclassrooms.eventorias.screen.LoginProvidersScreen
import com.openclassrooms.eventorias.screen.loginwithpassword.LoginWithPasswordScreen
import com.openclassrooms.eventorias.screen.recoveraccountwithmail.RecoverAccountWithMailScreen
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
                    LoginProvidersScreen { navHostController.navigate(Screen.LogInMailSelector.route) }
                }
                composable(Screen.LogInMailSelector.route) {
                    LoginMailSelectorScreen(
                        onCreateAccountClick = { navHostController.navigate(Screen.CreateAccountWithMail.route) },
                        onLogInClick = { navHostController.navigate(Screen.LogInWithPassword.route) },
                    )
                }
                composable(Screen.LogInWithPassword.route) {
                    LoginWithPasswordScreen(
                        onLogin = { navHostController.navigate(Screen.Home.route); Toast.makeText(
                            this@MainActivity,
                            getString(R.string.logged_in),
                            Toast.LENGTH_SHORT
                        ).show() },
                        onRecoverClick = { navHostController.navigate("${Screen.RecoverAccountWithMail.route}/${it}") },
                        onError = { navHostController.navigate(Screen.LogInProviders.route) ;Toast.makeText(this@MainActivity,
                            getString(R.string.connetion_error), Toast.LENGTH_SHORT).show() }
                    )
                }
                composable(Screen.CreateAccountWithMail.route) {
                    CreateAccountWithMailScreen(onLogin = { navHostController.navigate(Screen.Home.route); Toast.makeText(
                        this@MainActivity,
                        getString(R.string.logged_in),
                        Toast.LENGTH_SHORT
                    ).show() },
                        onError = { navHostController.navigate(Screen.LogInProviders.route) ;Toast.makeText(this@MainActivity,
                            getString(R.string.connetion_error), Toast.LENGTH_SHORT).show() })
                }
                composable(route = "${Screen.RecoverAccountWithMail.route}/{mail}",
                    arguments = Screen.RecoverAccountWithMail.navArguments) { backStackEntry ->
                    val mail = backStackEntry.arguments?.getString("mail") ?: ""
                    RecoverAccountWithMailScreen(
                        onRecover = {navHostController.navigate(Screen.LogInWithPassword.route)},
                        mail = mail
                    )
                }
            }
            composable(Screen.Home.route) {
                Text("In Home Screen")
            }
        }
    }
}
