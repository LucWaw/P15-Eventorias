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

    private var isSortedByDate = true

    init {
        viewModelScope.launch {
            eventRepository.events.collect {
                _events.value = it
            }
        }
    }

    fun filterEvents(filter : String) {
        viewModelScope.launch {
            eventRepository.events.collect {
                _events.value = it.filter { event -> event.title.contains(filter, ignoreCase = true) }
            }
        }
    }

    fun sortEvents(){
        if(isSortedByDate){
            sortItemsByTitle()
            isSortedByDate = false
        } else {
            sortItemsByDateDesc()
            isSortedByDate = true
        }
    }

    private fun sortItemsByDateDesc() {
        viewModelScope.launch {
            eventRepository.events.collect { event ->
                _events.value = event.sortedByDescending { it.eventDate }
            }
        }
    }

    private fun sortItemsByTitle() {
        viewModelScope.launch {
            eventRepository.events.collect { event ->
                _events.value = event.sortedBy { it.title }
            }
        }
    }
}