package com.openclassrooms.eventorias.screen.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Task
import com.openclassrooms.eventorias.data.UserRepository
import com.openclassrooms.eventorias.domain.User
import com.openclassrooms.eventorias.domain.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(val userRepository: UserRepository) : ViewModel() {

    private val _state: MutableStateFlow<UserState> = MutableStateFlow(UserState())
    val state: StateFlow<UserState>
        get() = _state

    init {
        viewModelScope.launch {
            loadUserData().collect {}
        }
    }

    fun loadUserData(): Flow<Result<User>> {
        return userRepository.getResultUserData().onEach {
            when (it) {
                is Result.Loading -> {
                    _state.update { currentState ->
                        currentState.copy(isLoading = true, isError = false)
                    }
                }

                is Result.Success -> {
                    _state.update { currentState ->
                        currentState.copy(isLoading = false, user = it.data, isError = false)
                    }


                }

                is Result.Error -> {
                    Log.e("ProfileViewModel", "Error loading events")

                    _state.update { currentState ->
                        currentState.copy(isLoading = false, isError = true)
                    }
                }
            }
        }
    }

    fun logout() {
        userRepository.signOut()
    }

    fun deleteCurrentUser(): Task<Void> {
        return userRepository.deleteUser()
    }


}