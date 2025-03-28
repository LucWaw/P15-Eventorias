package com.openclassrooms.eventorias.data

import android.net.Uri
import android.util.Log
import androidx.credentials.Credential
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.openclassrooms.eventorias.domain.User
import com.openclassrooms.eventorias.domain.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import java.util.UUID

class UserRepository {

    private val COLLECTION_NAME = "users"


    fun getCurrentUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }

    fun loginWithPassword(email: String, password: String): Task<AuthResult> {
        return FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
    }

    fun loginWithGoogle(credential: Credential): Task<AuthResult> {
        when (credential) {
            is GoogleIdTokenCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {

                        val googleIdTokenCredential = GoogleIdTokenCredential
                            .createFrom(credential.data)
                        val firebaseCredential =
                            GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)

                        return Firebase.auth.signInWithCredential(firebaseCredential)
                            .addOnSuccessListener {

                                val user = it.user
                                if (user != null) {
                                    userAlreadyExist(user.uid).addOnSuccessListener { userExist ->
                                        if (!userExist) {
                                            Log.d(
                                                "GooGleSignIn",
                                                "User does not exist, create user"
                                            )
                                            //Put display name and image url
                                            createUser(
                                                user.displayName ?: "",
                                                user.photoUrl?.toString(),
                                                true
                                            )
                                        }
                                    }
                                }
                            }

                    } catch (e: GoogleIdTokenParsingException) {
                        Log.e("GooGleSignIn", "Received an invalid google id token response", e)
                    }
                } else {
                    Log.e("GooGleSignIn", "Unexpected type of credential")
                }
            }

            else -> {
                Log.e("GooGleSignIn", "Unexpected type of credential : 64${credential.type}")
            }
        }
        return Tasks.forException(Exception("Invalid credential"))
    }

    private fun userAlreadyExist(id: String): Task<Boolean> {
        val docRef = getUsersCollection().document(id)

        return docRef.get().continueWith { task ->
            if (task.isSuccessful) {
                val document = task.result
                document != null && document.exists()
            } else {
                false
            }
        }
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

    /**
     * Delete the User from Firestore and Auth.
     * @return Task<Void> The task to delete the user.
     */
    fun deleteUser(): Task<Task<Void?>?> {
        // Delete user from Firestore
        return deleteUserFromFirestore().continueWith {
            // Delete the user account from the Auth
            getCurrentUser()?.delete()
        }
    }

    // Create User in Firestore
    private fun createUser(
        name: String,
        photoUrl: String? = null,
        isGoogleSignIn: Boolean = false
    ) {
        val user = getCurrentUser()
        if (user != null) {
            val uid = user.uid
            val email = user.email
            val userToCreate = User(
                uid = uid,
                displayName = name,
                email = email ?: "",
                urlPicture = photoUrl,
                googleSignIn = isGoogleSignIn
            )

            getUsersCollection().document(uid).set(userToCreate)
        }
    }

    private fun getUsersCollection(): CollectionReference {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME)
    }

    // Delete the User from Firestore
    private fun deleteUserFromFirestore(): Task<Void> {
        val uid: String? = getCurrentUserUID()
        if (uid != null) {
            return getUsersCollection().document(uid).delete()
        }
        return Tasks.forResult(null)
    }

    // Get User Data from Firestore
    fun getUserData(): Task<User>? {
        val uid: String? = getCurrentUserUID()
        return if (uid != null) {
            getUsersCollection().document(uid).get().continueWith { task: Task<DocumentSnapshot> ->
                task.result.toObject(
                    User::class.java
                )
            }
        } else {
            null
        }
    }

    fun getResultUserData(): Flow<Result<User>> = flow {
        emit(Result.Loading)

        val userDataTask = getUserData() ?: throw IllegalStateException("User data task is null")
        @Suppress("USELESS_ELVIS") val userData = userDataTask.await()
            ?: throw IllegalStateException("User data is null") //static analysis is wrong here, keep the ?: operator

        emit(Result.Success(userData))
    }.catch {
        emit(Result.Error)
    }.flowOn(Dispatchers.IO)


    // Get the current user's UID
    private fun getCurrentUserUID(): String? {
        val user: FirebaseUser? = getCurrentUser()
        return user?.uid
    }



    /**
     * Updates a user's information, potentially including their profile image.
     *
     * This function handles the logic for updating a user's data. It checks if a new profile image URI
     * is provided. If so, it calls the function to update the user with the new image.
     * Otherwise, it calls the function to update the user without changing the image.
     * It also verifies if a user is currently authenticated before proceeding.
     *
     * @param userBase The base user object containing the user's data before the update.
     *        userBase email will be used for verification if it differs from the currently authenticated user's email.
     * @param changedUri The URI of the new profile image. If `Uri.EMPTY`, no image update is performed.
     *        If a valid uri is provided then the image will be updated.
     *
     * @return A [Task] that completes when the user update operation is finished.
     *         - If the user is not authenticated, the Task will fail with an Exception containing the message "User not authenticated".
     *         - Otherwise the task will succeed once the update is done.
     *
     * @throws Exception If the user is not authenticated.
     *
     * @see updateUserWithImage
     * @see updateUserWithoutImage
     * @see getCurrentUser
     */
    fun updateUser(userBase: User, changedUri: Uri): Task<Void> {
        val currentUser = getCurrentUser() ?: return Tasks.forException(Exception("User not authenticated"))
        val user = userBase.copy(email = currentUser.email ?: "")

        if (changedUri != Uri.EMPTY) {
            return updateUserWithImage(user, changedUri, userBase.email, currentUser)
        }
        return updateUserWithoutImage(user, userBase.email, currentUser)
    }

    private fun updateUserWithImage(user: User, imageUri: Uri, newEmail: String, currentUser: FirebaseUser): Task<Void> {
        return uploadImage(imageUri)
            .onSuccessTask { it.storage.downloadUrl }
            .onSuccessTask { uri ->
                val userUpdated = user.copy(urlPicture = uri.toString())
                getUsersCollection().document(userUpdated.uid).set(userUpdated)
            }.continueWithTask {
                verifyEmailIfChanged(user.email, newEmail, currentUser)
            }
    }

    private fun updateUserWithoutImage(user: User, newEmail: String, currentUser: FirebaseUser): Task<Void> {
        return getUsersCollection().document(user.uid).set(user)
            .continueWithTask {
                verifyEmailIfChanged(user.email, newEmail, currentUser)
            }
    }

    private fun verifyEmailIfChanged(currentEmail: String, newEmail: String, user: FirebaseUser): Task<Void> {
        return if (currentEmail != newEmail) {
            user.verifyBeforeUpdateEmail(newEmail)
        } else {
            Tasks.forResult(null)
        }
    }



    fun uploadImage(imageUri: Uri): UploadTask {
        val uuid = UUID.randomUUID().toString() // GENERATE UNIQUE STRING
        val mImageRef = FirebaseStorage.getInstance().getReference("$COLLECTION_NAME/$uuid")
        return mImageRef.putFile(imageUri)
    }
}