package com.openclassrooms.eventorias.screen.profile

import android.net.Uri

/**
 * A sealed class representing different events that can occur on the profile screen data.
 */
sealed class ProfileAction {

    data class ImageChanged(val image: Uri) : ProfileAction()

    data class EmailChanged(val email: String) : ProfileAction()

    data class NameChanged(val name: String) : ProfileAction()
}