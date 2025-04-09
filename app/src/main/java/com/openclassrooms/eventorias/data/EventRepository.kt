package com.openclassrooms.eventorias.data

import android.net.Uri
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


    /**
     * Retrieves an Event from the backend based on the provided event ID.
     *
     * This function interacts with the `eventApi` to fetch event data associated with the given `eventId`.
     * It then attempts to convert the retrieved data to an `EventDto` object.
     * If the conversion is successful, it further transforms the `EventDto` to an `Event` object.
     *
     * @param eventId The unique identifier of the event to retrieve.
     * @return A `Task` that, upon successful completion, will contain the retrieved `Event` object.
     *         If an error occurs during data retrieval or conversion, the task will be completed with an exception.
     * @throws RuntimeException If the task result cannot be converted to an `EventDto` object.
     *                          The exception message indicates the failure to convert the task result.
     * @see EventDto
     * @see Event
     * @see eventApi
     */
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


    fun deleteEvent(eventId: String) : Task<Void> {
        return eventApi.deleteEvent(eventId)
    }

    /**
     * Adds a new Event to the data source using the injected EventApi.
     *
     * @param event The Post object to be added.
     * @param uri The URI of the image associated with the post.
     */
    fun addEvent(event: Event, uri: Uri?) {
        val eventDto = event.toDto()
        if (uri == null) {
            eventApi.addEvent(eventDto)
            return
        }
        eventApi.uploadImage(uri).addOnSuccessListener { taskSnapshot ->
            taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                val eventUpdatedURL = eventDto.copy(photoUrl = uri.toString())
                eventApi.addEvent(eventUpdatedURL)
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