package com.openclassrooms.eventorias.domain

import org.junit.Test
import com.openclassrooms.eventorias.data.entity.EventDto
import org.junit.Assert.assertEquals
import java.time.LocalDate
import java.time.LocalTime

class EventTest {

    @Test
    fun `Valid Event from Dto date conversion`() {
        // Test with a valid EventDto to ensure that all fields are 
        // correctly mapped to the Event object.

        // Given a valid EventDto
        val eventDto = EventDto(
            eventDate = 100
        )

        val date = LocalDate.of(1970, 4, 11)

        // When converting the EventDto to an Event object
        val event = Event.fromDto(eventDto)

        // Then
        assertEquals(date, event.eventDate)

    }

    @Test
    fun `valid Event From Dto Time Conversion`() {
        // Test with a valid EventDto to ensure that all fields are
        // correctly mapped to the Event object.

        // Given a valid EventDto
        val eventDto = EventDto(
            eventHours = 70490
        )

        val time = LocalTime.of(19, 34, 50)

        // When converting the EventDto to an Event object
        val event = Event.fromDto(eventDto)

        // Then
        assertEquals(time, event.eventHours)
    }


    @Test
    fun `valid Event to Dto date conversion`() {

        // Given a valid Event
        val event = Event(
            eventDate = LocalDate.of(1970, 4, 11),
        )
        val date = 100L

        // When converting the Event to an EventDto object
        val eventDto = event.toDto()

        // Then
        assertEquals(date, eventDto.eventDate)
    }

    @Test
    fun `valid Event to Dto Time Conversion`() {

        // Given a valid Event
        val event = Event(
            eventHours = LocalTime.of(19, 34, 50),
        )

        val time = 70490

        // When converting the Event to an EventDto object
        val eventDto = event.toDto()

        // Then
        assertEquals(time, eventDto.eventHours)

    }
}