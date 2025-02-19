package com.openclassrooms.eventorias.domain

import java.io.Serializable
import java.time.LocalDate
import java.time.LocalTime

/**
 * This class represents a Event data object. It holds information about a event, including its
 * ID, title, description, evenement date, evenement hours, evenement localisation, photo URL and the author (User object),
 * The class implements Serializable to allow for potential serialization needs.
 */
data class Event(
    val id: String,
    val title: String,
    val description: String,
    val eventDate: LocalDate,
    val eventHours: LocalTime,
    val eventLocation: String,
    val photoUrl: String,
    val author: User
) : Serializable
