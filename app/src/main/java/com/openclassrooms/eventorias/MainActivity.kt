package com.openclassrooms.eventorias

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.openclassrooms.eventorias.screen.LoginMailSelectorScreen
import com.openclassrooms.eventorias.screen.Screen
import com.openclassrooms.eventorias.screen.homefeed.HomeFeedScreen
import com.openclassrooms.eventorias.screen.login.createaccountwithmail.CreateAccountWithMailScreen
import com.openclassrooms.eventorias.screen.login.loginproviders.LoginProvidersScreen
import com.openclassrooms.eventorias.screen.login.loginwithpassword.LoginWithPasswordScreen
import com.openclassrooms.eventorias.screen.login.recoveraccountwithmail.RecoverAccountWithMailScreen
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
            navController = navHostController, startDestination = if (Firebase.auth.currentUser != null)Screen.Home.route else Screen.Auth.route
        ) {
            navigation(
                startDestination = Screen.LogInProviders.route,
                route = Screen.Auth.route
            ) {
                composable(Screen.LogInProviders.route) {
                    LoginProvidersScreen(
                        onMailClick = { navHostController.navigate(Screen.LogInMailSelector.route) },
                        onGoogleSignIn = {
                            navHostController.navigate(Screen.Home.route) {
                                popUpTo(Screen.LogInProviders.route) {
                                    inclusive = true
                                }//MARCHE BIEN
                            }
                        },
                    )
                }
                composable(Screen.LogInMailSelector.route) {
                    LoginMailSelectorScreen(
                        onCreateAccountClick = { navHostController.navigate(Screen.CreateAccountWithMail.route) },
                        onLogInClick = { navHostController.navigate(Screen.LogInWithPassword.route) },
                    )
                }
                composable(Screen.LogInWithPassword.route) {
                    LoginWithPasswordScreen(
                        onLogin = {
                            navHostController.navigate(Screen.Home.route) {
                                popUpTo(Screen.LogInProviders.route) { inclusive = true }
                            }; Toast.makeText(
                            this@MainActivity,
                            getString(R.string.logged_in),
                            Toast.LENGTH_SHORT
                        ).show()
                        },
                        onRecoverClick = { navHostController.navigate("${Screen.RecoverAccountWithMail.route}/${it}") },
                    )
                }
                composable(Screen.CreateAccountWithMail.route) {
                    CreateAccountWithMailScreen(onLogin = {
                        navHostController.navigate(Screen.Home.route) {
                            popUpTo(Screen.LogInProviders.route) { inclusive = true }
                        }; Toast.makeText(
                        this@MainActivity,
                        getString(R.string.logged_in),
                        Toast.LENGTH_SHORT
                    ).show()
                    },
                        onError = {
                            navHostController.navigate(Screen.LogInProviders.route);Toast.makeText(
                            this@MainActivity,
                            getString(R.string.connetion_error), Toast.LENGTH_SHORT
                        ).show()
                        })
                }
                composable(
                    route = "${Screen.RecoverAccountWithMail.route}/{mail}",
                    arguments = Screen.RecoverAccountWithMail.navArguments
                ) { backStackEntry ->
                    val mail = backStackEntry.arguments?.getString("mail") ?: ""
                    RecoverAccountWithMailScreen(
                        onRecover = { navHostController.navigate(Screen.LogInWithPassword.route) },
                        mailInit = mail,
                        onError = {
                            navHostController.navigate(Screen.LogInProviders.route);Toast.makeText(
                            this@MainActivity,
                            getString(R.string.send_mail_error), Toast.LENGTH_SHORT
                        ).show()
                        }
                    )
                }
            }
            composable(Screen.Home.route) {
                HomeFeedScreen()
            }
        }
    }
}
