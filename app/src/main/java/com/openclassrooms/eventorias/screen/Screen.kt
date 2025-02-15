package com.openclassrooms.eventorias.screen

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screen(
    val route: String,
    val navArguments: List<NamedNavArgument> = emptyList()
) {
    data object Auth : Screen("auth")

    data object LogInProviders : Screen("logInProviders")

    data object LogInWithMail : Screen("logInWithMail")

    data object LogInWithPassword : Screen("logInWithPassword", navArguments = listOf(navArgument("mail") { type = NavType.StringType }))

    data object CreateAccountWithMail : Screen("createAccountWithMail", navArguments = listOf(navArgument("mail") { type = NavType.StringType }))

    data object RecoverAccountWithMail : Screen("recoverAccountWithMail", navArguments = listOf(navArgument("mail") { type = NavType.StringType }))


}