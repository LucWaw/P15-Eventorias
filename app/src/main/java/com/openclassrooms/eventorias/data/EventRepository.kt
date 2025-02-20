package com.openclassrooms.eventorias.data

import com.openclassrooms.eventorias.data.service.EventFakeApi
import com.openclassrooms.eventorias.domain.Event
import com.openclassrooms.eventorias.domain.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class EventRepository (eventFakeApi: EventFakeApi) {

    /**
     * Retrieves a Flow object containing a list of Posts ordered by creation date
     * in descending order.
     *
     * @return Flow containing a list of Posts.
     */
    val events: Flow<Result<Flow<List<Event>>>> = flow {
        emit(Result.Loading)

        emit(Result.Success(eventFakeApi.getEventsOrderByEventDateDesc()))
    }.catch {
        emit(Result.Error)
    }.flowOn(Dispatchers.IO)


}