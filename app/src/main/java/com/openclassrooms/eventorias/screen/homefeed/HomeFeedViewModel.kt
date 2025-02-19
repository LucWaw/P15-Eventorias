package com.openclassrooms.eventorias.screen.homefeed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.eventorias.data.EventRepository
import com.openclassrooms.eventorias.domain.Event
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeFeedViewModel(val eventRepository: EventRepository) : ViewModel() {
    private val _events: MutableStateFlow<List<Event>> = MutableStateFlow(emptyList())
    val events: StateFlow<List<Event>>
        get() = _events

    init {
        viewModelScope.launch {
            eventRepository.posts.collect {
                _events.value = it
            }
        }
    }
}