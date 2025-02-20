package com.openclassrooms.eventorias.screen.homefeed

import com.openclassrooms.eventorias.domain.Event


data class EventListState(
    val isLoading: Boolean = false,
    val event: List<Event> = emptyList(),
    val isError: Boolean = false
)