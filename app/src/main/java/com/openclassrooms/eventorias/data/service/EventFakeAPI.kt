package com.openclassrooms.eventorias.data.service

import com.openclassrooms.eventorias.domain.Event
import com.openclassrooms.eventorias.domain.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.LocalDate
import java.time.LocalTime

class EventFakeApi {
    fun getEventsOrderByEventDateDesc(): Flow<List<Event>> {
        return events
    }

    private val users = mutableListOf(
        User("1", "Gerry", "https://picsum.photos/id/80/1080/"),
        User("2", "Brenton", "https://picsum.photos/id/80/1080/"),
        User("3", "Wally", "https://picsum.photos/id/80/1080/"),
    )

    private val events = MutableStateFlow(
        mutableListOf(
            Event(
                "5",
                "The Secret of the Flowers",
                "Improve your goldfish's physical fitness by getting him a bicycle.",
                LocalDate.of(2021, 8, 25),
                LocalTime.of(10, 0),
                "Location 1",
                "https://picsum.photos/id/80/1080/",
                users[0]
            ),
            Event(
                "4",
                "The Door's Game",
                "A great game for kids and adults.",
                LocalDate.of(2016, 1, 1),
                LocalTime.of(14, 30),
                "Location 2",
                "https://picsum.photos/id/85/1080/",
                users[2]
            ),
            Event(
                "1",
                "Laughing History",
                "He learned the important lesson that a picnic at the beach on a windy day is a bad idea.",
                LocalDate.of(2013, 2, 24),
                LocalTime.of(16, 0),
                "Location 3",
                "https://picsum.photos/id/80/1080/",
                users[0]
            ),
            Event(
                "3",
                "Woman of Years",
                "After fighting off the alligator, Brian still had to face the anaconda.",
                LocalDate.of(2012, 9, 2),
                LocalTime.of(11, 45),
                "Location 4",
                "https://picsum.photos/id/80/1080/",
                users[0]
            ),


            )
    )


}