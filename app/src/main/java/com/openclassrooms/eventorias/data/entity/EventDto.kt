package com.openclassrooms.eventorias.data.entity

import com.openclassrooms.eventorias.domain.User
import java.io.Serializable

data class EventDto(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val eventDate: Long = 0,
    val eventHours: Int = 0,
    val eventLocation: String = "",
    val photoUrl: String? = null,
    val author: User = User()
) : Serializable
