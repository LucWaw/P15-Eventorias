package com.openclassrooms.eventorias.data

import android.util.Log
import com.google.android.gms.tasks.Task
import com.openclassrooms.eventorias.data.entity.EventDto
import com.openclassrooms.eventorias.data.service.EventFirebaseApi
import com.openclassrooms.eventorias.domain.Event
import com.openclassrooms.eventorias.domain.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class EventRepository(private val eventApi: EventFirebaseApi) {
    fun getPost(eventId: String): Task<Event> {
        return eventApi.getPost(eventId).continueWith { task ->
            val eventDto = task.result.toObject(EventDto::class.java)
            if (eventDto == null){
                Log.e("EventRepository", "Failed to convert the task result to a EventDto object")
                throw RuntimeException("Failed to convert the task result to a EventDto object")
            }else{
                Event.fromDto(eventDto)
            }
        }
    }

    /**
     * Retrieves a Flow object containing a list of Event ordered by creation date
     * in descending order.
     *
     * @return Flow containing a list of Event.
     */
    val events: Flow<Result<Flow<List<Event>>>> = flow {
        emit(Result.Loading)

        emit(Result.Success(eventApi.getEventsOrderByEventDateDesc()))
    }.catch {
        emit(Result.Error)
    }.flowOn(Dispatchers.IO)


}