package com.openclassrooms.eventorias

import android.os.Bundle
import android.view.accessibility.AccessibilityManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.openclassrooms.eventorias.screen.Screen
import com.openclassrooms.eventorias.screen.addevent.AddEventScreen
import com.openclassrooms.eventorias.screen.detail.DetailScreen
import com.openclassrooms.eventorias.screen.homefeed.HomeFeedScreen
import com.openclassrooms.eventorias.screen.login.LoginMailSelectorScreen
import com.openclassrooms.eventorias.screen.login.createaccountwithmail.CreateAccountWithMailScreen
import com.openclassrooms.eventorias.screen.login.loginproviders.LoginProvidersScreen
import com.openclassrooms.eventorias.screen.login.loginwithpassword.LoginWithPasswordScreen
import com.openclassrooms.eventorias.screen.login.recoveraccountwithmail.RecoverAccountWithMailScreen
import com.openclassrooms.eventorias.screen.profile.ProfileScreen
import com.openclassrooms.eventorias.ui.theme.BlackBackground
import com.openclassrooms.eventorias.ui.theme.EventoriasTheme
import com.openclassrooms.eventorias.ui.theme.GreyLight
import org.koin.compose.KoinContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KoinContext {
                val navController = rememberNavController()

                val accessibilityManager =
                    this.getSystemService(ACCESSIBILITY_SERVICE) as AccessibilityManager

                EventoriasTheme {
                    BottomNavigation(navController) {
                        EventoriasNavHost(navController, it, accessibilityManager)
                    }

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
                            enabled = Firebase.auth.currentUser != null,
                            selected = currentDestination?.route == topLevelRoute.route,
                            onClick = {
                                val destination = if (Firebase.auth.currentUser != null) {
                                    topLevelRoute.route
                                } else {
                                    Screen.Auth.route
                                }
                                navController.navigate(destination) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                unselectedIconColor = GreyLight,
                                unselectedTextColor = GreyLight
                            )
                        )
                    }
                }
            }
        ) { innerPadding ->

            content(innerPadding)

        }
    }

    @Composable
    fun EventoriasNavHost(
        navHostController: NavHostController,
        innerPadding: PaddingValues,
        accessibilityManager: AccessibilityManager
    ) {
        Firebase.auth.addAuthStateListener { auth ->
            if (auth.currentUser == null) {
                navHostController.navigate(Screen.Auth.route) {
                    popUpTo(Screen.Profile.route) { inclusive = true }
                }
            }
        }


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
                    CreateAccountWithMailScreen(
                        onLogin = {
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
                HomeFeedScreen(
                    onPostClick = {
                        navHostController.navigate("${Screen.Detail.route}/${it}")
                    }, onAddClick = {
                        navHostController.navigate(Screen.AddEvent.route)
                    },
                    isAccessibilityEnabled = accessibilityManager.isEnabled && accessibilityManager.isTouchExplorationEnabled
                )
            }
            composable(
                route = "${Screen.Detail.route}/{eventId}",
                arguments = Screen.Detail.navArguments
            ) { backStackEntry ->
                val eventId = backStackEntry.arguments?.getString("eventId") ?: return@composable
                DetailScreen(
                    eventId = eventId,
                    onBackClick = { navHostController.navigateUp() }
                )
            }
            composable(Screen.Profile.route) {
                ProfileScreen(
                    onSignOut =
                        {
                            navHostController.navigate(Screen.Auth.route) {
                                popUpTo(navHostController.graph.id) {
                                    inclusive = true
                                }
                            }
                        })
            }
            composable(Screen.AddEvent.route) {
                AddEventScreen(
                    onBackClick = { navHostController.navigateUp() }
                )
            }

        }
    }


}
