package com.openclassrooms.eventorias.screen.login.loginproviders

import androidx.credentials.Credential
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.openclassrooms.eventorias.data.UserRepository

class LoginProvidersViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    fun loginWithGoogle(credentials : Credential) : Task<AuthResult>{
        return userRepository.loginWithGoogle(credentials)
    }


}