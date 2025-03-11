package com.openclassrooms.eventorias.data.service

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.FirebaseStorage
import com.openclassrooms.eventorias.data.entity.EventDto
import com.openclassrooms.eventorias.domain.Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

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
                querySnapshot.documents.map {
                    val eventDto = it.toObject(EventDto::class.java)
                    if (eventDto != null) {
                        eventDto.id = it.id
                    }
                    if (eventDto != null) {
                        Event.fromDto(eventDto)
                    } else {
                        throw RuntimeException("Failed to convert the task result to a EventDto object")
                    }
                }
            }
    }

    fun getPost(eventId: String): Task<DocumentSnapshot> {
        return getEventCollection().document(eventId).get()

    }

    fun uploadImage(imageUri: Uri): UploadTask {
        val uuid = UUID.randomUUID().toString() // GENERATE UNIQUE STRING
        val mImageRef = FirebaseStorage.getInstance().getReference("$COLLECTION_NAME/$uuid")
        return mImageRef.putFile(imageUri)
    }

    fun addEvent(eventDto: EventDto) {
        getEventCollection().add(eventDto)
    }
}