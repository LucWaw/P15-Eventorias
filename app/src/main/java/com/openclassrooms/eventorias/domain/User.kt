package com.openclassrooms.eventorias.domain

import androidx.annotation.Keep
import java.io.Serializable

@Keep
data class User(
    val uid: String = "",
    val displayName: String = "",
    val email : String = "",
    val urlPicture : String? = null,
    val googleSignIn: Boolean = false
) : Serializable {
    constructor() : this("", "", "", null, false)
}