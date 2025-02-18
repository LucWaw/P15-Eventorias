package com.openclassrooms.eventorias.screen.recoveraccountwithmail

import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.openclassrooms.eventorias.data.UserRepository

class RecoverAccountWithMailViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    fun recoverAccountWithMail(email : String): Task<Void> {
        return userRepository.sendRecoverMail(email)
    }
}