package com.openclassrooms.eventorias

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.openclassrooms.eventorias.screen.LoginMailSelectorScreen
import com.openclassrooms.eventorias.screen.Screen
import com.openclassrooms.eventorias.screen.component.WhiteButton
import com.openclassrooms.eventorias.screen.homefeed.HomeFeedScreen
import com.openclassrooms.eventorias.screen.login.createaccountwithmail.CreateAccountWithMailScreen
import com.openclassrooms.eventorias.screen.login.loginproviders.LoginProvidersScreen
import com.openclassrooms.eventorias.screen.login.loginwithpassword.LoginWithPasswordScreen
import com.openclassrooms.eventorias.screen.login.recoveraccountwithmail.RecoverAccountWithMailScreen
import com.openclassrooms.eventorias.ui.theme.BlackBackground
import com.openclassrooms.eventorias.ui.theme.EventoriasTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            EventoriasTheme {
                BottomNavigation(navController) {
                    EventoriasNavHost(navController, it)
                }

            }
        }
    }

    @Composable
    fun BottomNavigation(
        navController: NavHostController,
        content: @Composable (padding: PaddingValues) -> Unit
    ) {
        data class TopLevelRoute(val name: String, val route: String, val icon: Painter)

        val topLevelRoutes = listOf(
            TopLevelRoute(
                stringResource(R.string.events),
                Screen.Home.route,
                painterResource(R.drawable.icon_event)
            ),
            TopLevelRoute(
                stringResource(R.string.profile),
                Screen.Profile.route,
                painterResource(R.drawable.profile_icon)
            ),
        )
        Scaffold(
            bottomBar = {
                NavigationBar(
                    containerColor = BlackBackground
                ) {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination

                    topLevelRoutes.forEach { topLevelRoute ->
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    topLevelRoute.icon,
                                    contentDescription = topLevelRoute.name
                                )
                            },
                            label = { Text(topLevelRoute.name) },
                            selected = currentDestination?.route == topLevelRoute.route,
                            onClick = {
                                navController.navigate(topLevelRoute.route) {
                                    // Pop up to the start destination of the graph to
                                    // avoid building up a large stack of destinations
                                    // on the back stack as users select items
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    // Avoid multiple copies of the same destination when
                                    // reselecting the same item
                                    launchSingleTop = true
                                    // Restore state when reselecting a previously selected item
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        ) { innerPadding ->

            content(innerPadding)

        }
    }

    @Composable
    fun EventoriasNavHost(navHostController: NavHostController, innerPadding: PaddingValues) {
        NavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navHostController,
            startDestination = if (Firebase.auth.currentUser != null) Screen.Home.route else Screen.Auth.route
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
                                }
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
            composable(Screen.Profile.route) {
                Text(
                    text = "Profile",
                )
                WhiteButton(
                    modifier = Modifier
                        .padding(24.dp),
                    text = "Logout",
                    onClick = { Firebase.auth.signOut() }
                )
            }
        }
    }
}
