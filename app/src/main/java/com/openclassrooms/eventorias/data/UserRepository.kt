package com.openclassrooms.eventorias.data

import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
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
    private fun createUser(name: String, photoUrl: String? = null, isGoogleSignIn: Boolean = false) {
        val user = getCurrentUser()
        if (user != null) {
            val uid = user.uid
            val email = user.email
            val userToCreate = User(
                uid = uid, displayName = name, email = email ?: "", urlPicture = photoUrl, googleSignIn = isGoogleSignIn
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
     * Updates a user's information in the database.
     *
     * This function handles updating a user's data, including their profile picture if provided.
     * It first checks if a profile picture URL is supplied. If not, it updates the user's data
     * without any image-related operations. If a profile picture URL is provided, it uploads the
     * image, retrieves the download URL, and then updates the user's data with the new image URL.
     * After the user data update, it triggers an email verification process for the user if the
     * email was modified.
     *
     * @param userBase The base User object containing the updated user information.
     *                 It includes the user's UID, name, email, and potentially a new profile picture URL.
     *                 The [userBase.email] will be used for email update verification, and the
     *                 [userBase.urlPicture] will be processed for uploading if it is not null or empty.
     * @return A Task that represents the asynchronous operation of updating the user.
     *         The outer Task will resolve to another inner Task. The inner Task represents the result of the update operation.
     *         - If the update is successful, the inner Task will be successful.
     *         - If an error occurs during the update, the inner Task will fail with an exception.
     *         - If there is no image to upload, the inner task will contain the result of the user data set operation.
     *         - If there is an image to upload, the inner task will be the result of the image upload and the user data set operation
     *          including the downloaded URL.
     *         The outer Task will also contain an error if any operation has failed.
     *
     * @throws Exception if an unknown error occurs during image upload or user data update.
     *
     * Note: This function relies on the following external dependencies:
     * - `getCurrentUser()`: A function that returns the currently authenticated user.
     * - `getUsersCollection()`: A function that returns a reference to the users collection in the database.
     */
    fun updateUser(userBase: User): Task<out Task<out Any?>?> {
        val user = userBase.copy(email = getCurrentUser()?.email ?: "") // Email update after user auth validation

        return if (userBase.urlPicture.isNullOrEmpty()) {
            // If the image is empty, we return a simple user update task without an image
            val updateTask = getUsersCollection().document(user.uid).set(user).continueWith {
                getCurrentUser()?.verifyBeforeUpdateEmail(userBase.email)
            }
            Tasks.forResult(updateTask) // Wrap the result in a Task<Task<Void?>?>
        } else {
            // If the image is supplied, proceed to image download
            val uploadTask = uploadImage(userBase.urlPicture.toUri()).continueWithTask { task ->
                if (task.isSuccessful) {
                    task.result?.storage?.downloadUrl?.addOnSuccessListener { uri ->
                        val userUpdatedUri = user.copy(urlPicture = uri.toString())
                        // Update user with image URL
                        getUsersCollection().document(userUpdatedUri.uid).set(userUpdatedUri).continueWith {
                            getCurrentUser()?.verifyBeforeUpdateEmail(userBase.email)
                        }
                    }
                } else {
                    Tasks.forException<Void>(task.exception ?: Exception("Unknown error"))
                }
            }
            Tasks.forResult(uploadTask) // Wrap the result in a Task<Task<Void?>?>
        }
    }

    fun uploadImage(imageUri: Uri): UploadTask {
        val uuid = UUID.randomUUID().toString() // GENERATE UNIQUE STRING
        val mImageRef = FirebaseStorage.getInstance().getReference("$COLLECTION_NAME/$uuid")
        return mImageRef.putFile(imageUri)
    }
}