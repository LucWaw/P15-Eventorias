package com.openclassrooms.eventorias.screen.profile

import android.net.Uri
import com.openclassrooms.eventorias.domain.User

data class UserState(
    val isLoading: Boolean = false,
    val user: User = User(),
    val isError: Boolean = false,
    val notification : Boolean = false,
    val changedUri : Uri = Uri.EMPTY
)
