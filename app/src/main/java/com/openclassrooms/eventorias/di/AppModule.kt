package com.openclassrooms.eventorias.di

import org.koin.dsl.module
import com.openclassrooms.eventorias.screen.createaccountwithmail.CreateAccountWithMailViewModel
import com.openclassrooms.eventorias.screen.loginwithpassword.LoginWithPasswordViewModel
import com.openclassrooms.eventorias.screen.recoveraccountwithmail.RecoverAccountWithMailViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import com.openclassrooms.eventorias.data.UserRepository

val appModule = module {
    singleOf(::UserRepository)
    viewModelOf(::CreateAccountWithMailViewModel)
    viewModelOf(::LoginWithPasswordViewModel)
    viewModelOf(::RecoverAccountWithMailViewModel)
}