package com.openclassrooms.eventorias.screen.addevent

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Task
import com.openclassrooms.eventorias.data.EventRepository
import com.openclassrooms.eventorias.data.UserRepository
import com.openclassrooms.eventorias.domain.Event
import com.openclassrooms.eventorias.domain.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.util.Locale
import java.util.concurrent.CountDownLatch

class AddEventViewModel(
    private val eventRepository: EventRepository,
    val userRepository: UserRepository
) : ViewModel() {
    /**
     * Internal mutable state flow representing the current event being edited.
     */
    private var _event = MutableStateFlow(Event())

    /**
     * Public state flow representing the current event being edited.
     * This is immutable for consumers.
     */
    val event: StateFlow<Event>
        get() = _event

    private val _uriImage: MutableStateFlow<Uri?> = MutableStateFlow(null)
    val uriImage = _uriImage.asStateFlow()

    /**
     * StateFlow derived from the post that emits a AddPostFormError if the title is empty or the description is empty or
     * the localTime is empty or the localDate is empty or the address is empty or null otherwise.
     */
    val error = event.map {
        verifyPost()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = null,
    )

    /**
     * Handles form events like title and description changes.
     *
     * @param formEvent The form event to be processed.
     */
    fun onAction(formEvent: AddPostFormEvent) {
        when (formEvent) {
            is AddPostFormEvent.DescriptionChanged -> {
                _event.value = _event.value.copy(
                    description = formEvent.description
                )
            }

            is AddPostFormEvent.TitleChanged -> {
                _event.value = _event.value.copy(
                    title = formEvent.title
                )
            }

            is AddPostFormEvent.ImageChanged -> {
                _uriImage.value = formEvent.image
            }

            is AddPostFormEvent.LocalTimeChanged -> {
                _event.value = _event.value.copy(
                    eventHours = formEvent.localTime
                )
            }

            is AddPostFormEvent.LocalDateChanged -> {
                _event.value = _event.value.copy(
                    eventDate = formEvent.localDate
                )
            }

            is AddPostFormEvent.AddressChanged -> {
                _event.value = _event.value.copy(
                    eventLocation = formEvent.address
                )
            }
        }
    }

    /**
     * Attempts to add the current post to the repository after setting the author and call getLatitudeAndLongitudeFromAddressName
     *
     */
    fun addPost(context: Context): Task<User>? {
        val userData = userRepository.getUserData()

        if (userData == null) {
            Log.d("Screen AddEventViewModel", "User data is null")
            return null
        }

        return userData.addOnSuccessListener { user ->
            _event.value = _event.value.copy(
                author = user
            )
            val pair = getLatitudeAndLongitudeFromAddressName(context, _event.value.eventLocation)

            if (pair != null) {
                _event.value = _event.value.copy(
                    latitude = pair.first,
                    longitude = pair.second
                )
            }
            eventRepository.addEvent(
                _event.value,
                _uriImage.value
            )


        }.addOnFailureListener {
            Log.d("Screen AddEventViewModel", "Error: $it")
        }
    }

    /**
     * Verifies mandatory fields of the post
     * and returns a corresponding FormError if so.
     *
     * @return A FormError.TitleError if title is empty.
     * @return A FormError.descriptionError if description is empty.
     * @return A FormError.localTimeError if localTime is empty.
     * @return A FormError.localDateError if localDate is empty.
     * @return A FormError.addressError if address is empty.
     * @return null if all mandatory fields are filled.
     */
    private fun verifyPost(): AddPostFormError? {
        return when {
            _event.value.title.isEmpty() -> AddPostFormError.TitleError
            _event.value.description.isEmpty() -> AddPostFormError.DescriptionError
            _event.value.eventDate == null -> AddPostFormError.LocalDateError
            _event.value.eventHours == null -> AddPostFormError.LocalTimeError
            _event.value.eventLocation.isEmpty() -> AddPostFormError.AddressError
            else -> null
        }
    }


    /**
     * Retrieves the latitude and longitude coordinates for a given address name.
     *
     * This function uses the Geocoder to perform geocoding, which is the process of
     * converting an address string into geographic coordinates (latitude and longitude).
     *
     * @param context The application context. Required for accessing the Geocoder.
     * @param address The address string to geocode (e.g., "1600 Amphitheatre Parkway, Mountain View, CA").
     * @return A Pair containing the latitude and longitude as Doubles, or null if:
     *         - The device is running an Android version below API 33 (Tiramisu). Geocoding only works on API 33 and above on this function.
     *         - The address cannot be found or resolved by the Geocoder.
     *         - An error occurs during the geocoding process.
     *
     * @OptIn(ExperimentalCoroutinesApi::class) Marks this function as using experimental coroutines features.
     */
    fun getLatitudeAndLongitudeFromAddressName(
        context: Context,
        address: String
    ): Pair<Double, Double>? {
        val geocoder = Geocoder(context, Locale.getDefault())

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val latch = CountDownLatch(1)
            var result: Pair<Double, Double>? = null

            geocoder.getFromLocationName(
                address, 1,
                object : Geocoder.GeocodeListener {
                    override fun onGeocode(addresses: MutableList<Address>) {
                        if (addresses.isNotEmpty()) {
                            val firstAddress = addresses[0]
                            result = Pair(firstAddress.latitude, firstAddress.longitude)
                        }
                        latch.countDown() // Libère le thread bloqué
                    }

                    override fun onError(errorMessage: String?) {
                        super.onError(errorMessage)
                        latch.countDown() // Libère le thread bloqué en cas d'erreur
                    }
                })

            latch.await() // Bloque le thread principal jusqu'à ce que `countDown()` soit appelé
            return result
        }

        return null
    }

}