package com.openclassrooms.eventorias.screen.homefeed

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.eventorias.data.EventRepository
import com.openclassrooms.eventorias.domain.Event
import com.openclassrooms.eventorias.domain.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class HomeFeedViewModel(private val eventRepository: EventRepository) : ViewModel() {
    private val _state: MutableStateFlow<EventListState> = MutableStateFlow(EventListState(isLoading = true))
    val state: StateFlow<EventListState>
        get() = _state

    private var isSortedByDate = true

    init {
        viewModelScope.launch {
            loadEvent().collect {}
        }
    }

    fun loadEvent(): Flow<Result<Flow<List<Event>>>> {
        return eventRepository.events.onEach {
            when (it) {
                is Result.Loading -> {
                    _state.update { currentState ->
                        currentState.copy(isLoading = true, isError = false)
                    }
                }

                is Result.Success -> {
                    it.data.collect { event ->
                        _state.update { currentState ->
                            currentState.copy(isLoading = false, event = event, isError = false)
                        }
                    }

                }

                is Result.Error -> {
                    Log.e("HomeFeedViewModel", "Error loading events")

                    _state.update { currentState ->
                        currentState.copy(isLoading = false, isError = true)
                    }
                }
            }
        }
    }

    fun filterEvents(filter: String) {
        viewModelScope.launch {
            eventRepository.events.collect { result ->
                if (result is Result.Success) {
                    result.data.collect {
                        _state.update { currentState ->
                            currentState.copy(event = it.filter {
                                it.title.contains(
                                    filter,
                                    ignoreCase = true
                                )
                            })
                        }
                    }
                }
            }
        }

    }

    fun sortEvents() {
        if (isSortedByDate) {
            sortItemsByTitle()
            isSortedByDate = false
        } else {
            sortItemsByDateDesc()
            isSortedByDate = true
        }
    }

    private fun sortItemsByDateDesc() {

        viewModelScope.launch {
            eventRepository.events.collect { result ->
                if (result is Result.Success) {
                    result.data.collect {
                        _state.update { currentState ->
                            currentState.copy(event = it.sortedBy { it.eventDate })
                        }
                    }
                }
            }
        }
    }

    private fun sortItemsByTitle() {
        viewModelScope.launch {
            eventRepository.events.collect { result ->
                if (result is Result.Success) {
                    result.data.collect {
                        _state.update { currentState ->
                            currentState.copy(event = it.sortedBy { it.title })
                        }
                    }
                }
            }
        }
    }
}