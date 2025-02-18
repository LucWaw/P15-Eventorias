package com.openclassrooms.eventorias.data

import android.content.Context
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.openclassrooms.eventorias.domain.User

class UserRepository {

    private val COLLECTION_NAME = "users"


    fun getCurrentUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }

    fun loginWithPassword(email: String, password: String, name: String = ""): Task<AuthResult> {
        return FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
    }

    private fun createWithPassword(email: String, password: String): Task<AuthResult> {
        return FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
    }

    fun createWithPassword(email: String, password: String, name: String): Task<AuthResult> {
        return createWithPassword(email, password).addOnSuccessListener {
            createUser(name)
        }
    }

    fun sendRecoverMail(email: String): Task<Void> {
        return FirebaseAuth.getInstance().sendPasswordResetEmail(email)
    }

    fun signOut() {
        return FirebaseAuth.getInstance().signOut()
    }

    fun deleteUser(context: Context): Task<Void> {
        return getCurrentUser()?.delete() ?: Tasks.forResult(null)
    }

    // Create User in Firestore
    private fun createUser(name: String) {
        val user = getCurrentUser()
        if (user != null) {
            val uid = user.uid
            val userToCreate = User(uid, name)

            getUsersCollection().document(uid).set(userToCreate)
        }
    }

    private fun getUsersCollection(): CollectionReference {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME)
    }

    // Delete the User from Firestore
    fun deleteUserFromFirestore(): Task<Void> {
        val uid: String? = getCurrentUserUID()
        if (uid != null) {
            return getUsersCollection().document(uid).delete()
        }
        return Tasks.forResult(null)
    }

    // Get User Data from Firestore
    fun getUserData(): Task<DocumentSnapshot>? {
        val uid: String? = getCurrentUserUID()
        return if (uid != null) {
            getUsersCollection().document(uid).get()
        } else {
            null
        }
    }

    // Get the current user's UID
    private fun getCurrentUserUID(): String? {
        val user: FirebaseUser? = getCurrentUser()
        return user?.uid
    }
}