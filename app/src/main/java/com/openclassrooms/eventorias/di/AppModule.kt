package com.openclassrooms.eventorias.di

import org.koin.dsl.module
import com.openclassrooms.eventorias.screen.login.createaccountwithmail.CreateAccountWithMailViewModel
import com.openclassrooms.eventorias.screen.login.loginwithpassword.LoginWithPasswordViewModel
import com.openclassrooms.eventorias.screen.login.recoveraccountwithmail.RecoverAccountWithMailViewModel
import com.openclassrooms.eventorias.screen.login.loginproviders.LoginProvidersViewModel
import com.openclassrooms.eventorias.screen.homefeed.HomeFeedViewModel
import com.openclassrooms.eventorias.screen.detail.DetailViewModel
import com.openclassrooms.eventorias.screen.addevent.AddEventViewModel
import com.openclassrooms.eventorias.data.EventRepository
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import com.openclassrooms.eventorias.data.UserRepository
import com.openclassrooms.eventorias.data.service.EventFirebaseApi

val appModule = module {
    singleOf(::EventFirebaseApi)
    singleOf(::EventRepository)
    singleOf(::UserRepository)

    viewModelOf(::AddEventViewModel)
    viewModelOf(::CreateAccountWithMailViewModel)
    viewModelOf(::DetailViewModel)
    viewModelOf(::LoginWithPasswordViewModel)
    viewModelOf(::RecoverAccountWithMailViewModel)
    viewModelOf(::LoginProvidersViewModel)
    viewModelOf(::HomeFeedViewModel)
}
