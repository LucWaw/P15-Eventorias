package com.openclassrooms.eventorias.screen.login.createaccountwithmail

import androidx.annotation.StringRes
import com.openclassrooms.eventorias.R

/**
 * A sealed class representing different events that can occur on a form.
 */
sealed class FormEvent {


    data class EmailChanged(val email: String) : FormEvent()

    data class PasswordChanged(val password: String) : FormEvent()

    data class NameChanged(val name: String) : FormEvent()


}

sealed class FormError(@StringRes val messageRes: Int) {

    data object EmailError : FormError(
        R.string.error_email
    )

    data object PasswordError : FormError(
        R.string.error_password
    )

    data object NameError : FormError(
        R.string.error_name
    )
}
