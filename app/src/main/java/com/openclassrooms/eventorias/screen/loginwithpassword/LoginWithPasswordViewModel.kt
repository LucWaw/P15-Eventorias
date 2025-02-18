package com.openclassrooms.eventorias.screen.loginwithpassword

import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.openclassrooms.eventorias.data.UserRepository

class LoginWithPasswordViewModel(
    private val userRepository: UserRepository
) : ViewModel(){

    fun login(email: String, password: String): Task<AuthResult> {
        return userRepository.loginWithPassword(email, password)
    }
}