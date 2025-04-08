package com.openclassrooms.eventorias.screen.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import com.openclassrooms.eventorias.data.EventRepository
import com.openclassrooms.eventorias.domain.Event
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class DetailViewModel(private val eventRepository: EventRepository) : ViewModel() {
    private val _event = MutableStateFlow(Event())
    val event = _event.asStateFlow()

    fun loadEvent(postId: String) {
        val eventData = eventRepository.getPost(postId)

        eventData.addOnSuccessListener { event ->
            _event.value = event

        }.addOnFailureListener {
            Log.d("DetailViewModel", "Error getting event data", it)
        }
    }

    fun deleteEvent(eventId: String): com.google.android.gms.tasks.Task<Void> {
        return eventRepository.deleteEvent(eventId)
    }
}