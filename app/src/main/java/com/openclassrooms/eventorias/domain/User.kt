package com.openclassrooms.eventorias.domain

import java.io.Serializable

data class User(
    val uid: String = "",
    val displayName: String = "",
    val email : String = "",
    val urlPicture : String? = null,
    val googleSignIn: Boolean = false
) : Serializable
