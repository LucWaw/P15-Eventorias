package com.openclassrooms.eventorias.screen.createaccountwithmail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.openclassrooms.eventorias.data.UserRepository
import com.openclassrooms.eventorias.extension.StringExt.Companion.isValidEmail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class CreateAccountWithMailViewModel(
    private val userRepository: UserRepository
) : ViewModel() {
    fun createAccount(email: String, password: String, name: String): Task<AuthResult> {
        return userRepository.createWithPassword(email, password, name)
    }

    /**
     * Internal mutable state flow representing the current post being edited.
     */
    private var _user = MutableStateFlow(
        UserVerfication(
            name = "", email = "", password = ""
        )
    )

    /**
     * Public state flow representing the current post being edited.
     * This is immutable for consumers.
     */
    val user: StateFlow<UserVerfication>
        get() = _user

    /**
     * StateFlow derived from the post that emits a FormError if the title is empty, null otherwise.
     */
    val error = user.drop(1).map {
            verifyPost()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null,
        )

    private fun verifyPost(): FormError? {
        return when {
            !_user.value.email.isValidEmail() -> FormError.EmailError
            _user.value.name.isEmpty() -> FormError.NameError
            _user.value.password.length < 6 -> FormError.PasswordError

            else -> null
        }
    }

    /**
     * Handles form events like title and description changes.
     *
     * @param formEvent The form event to be processed.
     */
    fun onAction(formEvent: FormEvent) {
        when (formEvent) {
            is FormEvent.EmailChanged -> {
                _user.value = _user.value.copy(
                    email = formEvent.email
                )
            }

            is FormEvent.NameChanged -> {
                _user.value = _user.value.copy(
                    name = formEvent.name
                )
            }

            is FormEvent.PasswordChanged -> {
                _user.value = _user.value.copy(password = formEvent.password)
            }
        }
    }
}

data class UserVerfication(
    val name: String, val email: String, val password: String
)