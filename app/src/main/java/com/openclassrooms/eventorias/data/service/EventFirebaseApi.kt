package com.openclassrooms.eventorias.data.service

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots
import com.openclassrooms.eventorias.data.entity.EventDto
import com.openclassrooms.eventorias.domain.Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class EventFirebaseApi {
    private val COLLECTION_NAME = "events"

    private fun getEventCollection(): CollectionReference {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME)
    }

    fun getEventsOrderByEventDateDesc(): Flow<List<Event>> {
        return getEventCollection()
            .orderBy("eventDate", Query.Direction.DESCENDING)
            .snapshots()
            .map { querySnapshot ->
                querySnapshot.toObjects(EventDto::class.java).map {
                    Event.fromDto(it)
                }
            }
    }
}