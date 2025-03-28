package com.openclassrooms.eventorias.data.entity

import androidx.annotation.Keep
import com.openclassrooms.eventorias.domain.User
import java.io.Serializable

@Keep
data class EventDto(
    var id: String = "",
    val title: String = "",
    val description: String = "",
    val eventDate: Long = 0,
    val eventHours: Int = 0,
    val eventLocation: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val photoUrl: String? = null,
    val author: User = User()

) : Serializable {
    constructor() : this("", "", "", 0, 0, "", 0.0, 0.0, null, User())
}
