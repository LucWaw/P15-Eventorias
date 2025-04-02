package com.openclassrooms.eventorias.screen.profile

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.openclassrooms.eventorias.data.UserRepository
import com.openclassrooms.eventorias.domain.User
import com.openclassrooms.eventorias.domain.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(val userRepository: UserRepository, application: Application) :
    AndroidViewModel(application) {

    private val _state: MutableStateFlow<UserState> = MutableStateFlow(UserState())
    val state: StateFlow<UserState>
        get() = _state

    private val notificationManagerHelper =
        NotificationService(getApplication<Application>().applicationContext)

    init {
        viewModelScope.launch {
            loadUserData().collect {}
        }
    }

    fun updateFirestoreMail() {
        // if email in firestore is different from email in auth, update email in firestore
        val user = userRepository.getCurrentUser()

        if (user != null) {
            val email = user.email
            if (email != null) {
                val userRef = FirebaseFirestore.getInstance().collection("users").document(user.uid)
                userRef.get().addOnSuccessListener { document ->
                    if (document.exists()) {
                        val user = document.toObject(User::class.java)
                        if (user != null && user.email != email) {
                            userRef.update("email", email)
                            onAction(ProfileAction.EmailChanged(email))
                        }
                    }
                }
            }
        }
    }

    /**
     * Handles profile events like email, image and name changes.
     */
    fun onAction(profileAction: ProfileAction) {
        when (profileAction) {
            is ProfileAction.EmailChanged -> {
                _state.update { currentState ->
                    //update user.email with the new email
                    currentState.copy(user = currentState.user.copy(email = profileAction.email))
                }
            }

            is ProfileAction.ImageChanged -> {
                _state.update { currentState ->
                    //update user.urlPicture with the new image
                    currentState.copy(changedUri =  profileAction.image)
                }
            }

            is ProfileAction.NameChanged -> {
                _state.update { currentState ->
                    //update user.name with the new name
                    currentState.copy(user = currentState.user.copy(displayName = profileAction.name))
                }
            }
        }
    }

    fun updateProfileInfo(): Task<Void> {
        return userRepository.updateUser(state.value.user, state.value.changedUri)
    }


    fun onNotificationClicked(checked: Boolean) {
        _state.update { currentState ->
            currentState.copy(notification = checked)
        }

        if (state.value.notification) {
            enableNotifications()


        } else {
            disableNotifications()
        }
    }

    /**
     * Enables notifications for the application.
     */
    fun enableNotifications() {
        notificationManagerHelper.enableNotifications()
        FirebaseMessaging.getInstance().subscribeToTopic("All")
    }

    /**
     * Disables notifications for the application.
     */
    fun disableNotifications() {
        notificationManagerHelper.disableNotifications()

        //Suppress autorisation for Firebase Messaging
        FirebaseMessaging.getInstance().unsubscribeFromTopic("All")
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
                    Log.e("ProfileViewModel", "Error loading user data")

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

    fun deleteCurrentUser(): Task<Task<Void?>?> {
        return userRepository.deleteUser()
    }


}