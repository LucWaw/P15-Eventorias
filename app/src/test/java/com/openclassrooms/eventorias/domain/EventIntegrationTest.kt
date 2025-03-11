package com.openclassrooms.eventorias.domain

import org.junit.Test
import java.time.LocalDate
import org.junit.Assert.assertEquals
import java.time.LocalTime

class EventIntegrationTest {

    @Test
    fun `event with date toDto FromTto should return event with date`(){
        //given
        val date = LocalDate.now()

        val event = Event(eventDate = date)

        //when
        val dto = event.toDto()
        val eventFromDto = Event.fromDto(dto)

        //then
        assertEquals(date, eventFromDto.eventDate)
    }

    @Test
    fun `event with hours toDto FromTto should return event with hours`(){
        //given
        val hours = LocalTime.now()

        val event = Event(eventHours = hours)

        //when
        val dto = event.toDto()
        val eventFromDto = Event.fromDto(dto)

        //then
        assertEquals(hours.hour, eventFromDto.eventHours?.hour)
        assertEquals(hours.minute, eventFromDto.eventHours?.minute)
    }

}