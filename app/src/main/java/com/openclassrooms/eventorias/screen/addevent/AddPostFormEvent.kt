package com.openclassrooms.eventorias.screen.addevent

import android.net.Uri
import androidx.annotation.StringRes
import com.openclassrooms.eventorias.R
import java.time.LocalDate
import java.time.LocalTime


/**
 * A sealed class representing different events that can occur on the add post form.
 */
sealed class AddPostFormEvent {

    data class DescriptionChanged(val description: String) : AddPostFormEvent()

    data class TitleChanged(val title: String) : AddPostFormEvent()

    data class ImageChanged(val image: Uri) : AddPostFormEvent()

    data class LocalTimeChanged(val localTime: LocalTime) : AddPostFormEvent()

    data class LocalDateChanged(val localDate: LocalDate) : AddPostFormEvent()

    data class AddressChanged(val address: String) : AddPostFormEvent()
}

sealed class AddPostFormError(@StringRes val messageRes: Int) {

    data object TitleError : AddPostFormError(
        R.string.error_title
    )

    data object DescriptionError : AddPostFormError(
        R.string.error_description
    )

    data object LocalDateError : AddPostFormError(
        R.string.error_local_date
    )

    data object LocalTimeError : AddPostFormError(
        R.string.error_local_time
    )

    data object AddressError : AddPostFormError(
        R.string.error_address
    )
}