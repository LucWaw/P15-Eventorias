package com.openclassrooms.eventorias.screen

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screen(
    val route: String,
    val navArguments: List<NamedNavArgument> = emptyList()
) {
    data object Home : Screen("home")

    data object Detail : Screen("detail", navArguments = listOf(navArgument("eventId") { type = NavType.StringType }))

    data object Profile : Screen("profile")

    data object Auth : Screen("auth")

    data object LogInProviders : Screen("logInProviders")

    data object LogInMailSelector : Screen("logInMailSelector")

    data object LogInWithPassword : Screen("logInWithPassword")

    data object CreateAccountWithMail : Screen("createAccountWithMail")

    data object RecoverAccountWithMail : Screen("recoverAccountWithMail", navArguments = listOf(navArgument("mail") { type = NavType.StringType }))


}