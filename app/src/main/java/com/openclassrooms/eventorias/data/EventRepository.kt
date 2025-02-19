package com.openclassrooms.eventorias.data

import com.openclassrooms.eventorias.data.service.EventFakeApi
import com.openclassrooms.eventorias.domain.Event
import kotlinx.coroutines.flow.Flow

class EventRepository (eventFakeApi: EventFakeApi) {

    /**
     * Retrieves a Flow object containing a list of Posts ordered by creation date
     * in descending order.
     *
     * @return Flow containing a list of Posts.
     */
    val events: Flow<List<Event>> = eventFakeApi.getEventsOrderByEventDateDesc()

    //TODO ERROR AND LOADING STATE

}