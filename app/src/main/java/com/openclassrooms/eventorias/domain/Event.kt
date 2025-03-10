package com.openclassrooms.eventorias.domain

import com.openclassrooms.eventorias.data.entity.EventDto
import java.time.LocalDate
import java.time.LocalTime

/**
 * This class represents a Event data object. It holds information about a event, including its
 * ID, title, description, evenement date, evenement hours, evenement localisation, photo URL and the author (User object),
 * The class implements Serializable to allow for potential serialization needs.
 */
data class Event(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val eventDate: LocalDate? = LocalDate.now(),
    val eventHours: LocalTime? = LocalTime.now(),
    val eventLocation: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val photoUrl: String? = null,
    val author: User = User()
) {
    fun toDto() = EventDto(
        id = id,
        title = title,
        description = description,
        eventDate = eventDate?.toEpochDay() ?: 0,
        eventHours = eventHours?.toSecondOfDay() ?: 0,
        eventLocation = eventLocation,
        latitude = latitude,
        longitude = longitude,
        photoUrl = photoUrl,
        author = author
    )

    companion object {
        fun fromDto(dto: EventDto) = Event(
            id = dto.id,
            title = dto.title,
            description = dto.description,
            eventDate = LocalDate.ofEpochDay(dto.eventDate),
            eventHours = LocalTime.ofSecondOfDay(dto.eventHours.toLong()),
            eventLocation = dto.eventLocation,
            latitude = dto.latitude,
            longitude = dto.longitude,
            photoUrl = dto.photoUrl,
            author = dto.author
        )
    }
}
