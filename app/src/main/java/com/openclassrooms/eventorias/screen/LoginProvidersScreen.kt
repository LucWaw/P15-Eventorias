package com.openclassrooms.eventorias.screen

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.openclassrooms.eventorias.R
import com.openclassrooms.eventorias.screen.component.ButtonWithEmailIcon
import com.openclassrooms.eventorias.screen.component.ButtonWithGoogleIcon
import com.openclassrooms.eventorias.ui.theme.EventoriasTheme
import kotlinx.coroutines.launch

@Composable
fun LoginProvidersScreen(
    modifier: Modifier = Modifier,
    onMailClick: () -> Unit,
    onGoogleSignIn: () -> Unit
) {

    Scaffold(modifier = modifier) { innerPadding ->
        LoginProviders(modifier = Modifier.padding(innerPadding), onMailClick, onGoogleSignIn)
    }

}

@Composable
fun LoginProviders(
    modifier: Modifier = Modifier,
    onMailClick: () -> Unit,
    onGoogleSignIn: () -> Unit
) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = modifier.width(242.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_eventorias),
                contentDescription = "Eventorias logo",
                modifier = modifier
            )
            Spacer(Modifier.height(44.dp))
            val context = LocalContext.current
            val coroutineScope = rememberCoroutineScope()
            ButtonWithGoogleIcon(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    coroutineScope.launch {
                        signInWithGoogle(context, onGoogleSignIn)
                    }
                }
            )

            ButtonWithEmailIcon(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onMailClick() }
            )
        }
    }

}

suspend fun signInWithGoogle(context: Context, onGoogleSignIn: () -> Unit) {

    val googleIdOption: GetSignInWithGoogleOption =
        GetSignInWithGoogleOption.Builder("710750610457-6j9ib7dnt3o5b9g3cjiqsldcjb4on0ks.apps.googleusercontent.com")
            .build()

    val request: GetCredentialRequest = GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()

    val credentialManager: CredentialManager = CredentialManager.create(context)


    val credential = try {
        credentialManager.getCredential(context, request).credential
    } catch (e: Exception) {
        Log.e("GooGleSignIn", "Error getting Google credential", e)
        return
    }

    handleSignIn(credential, onGoogleSignIn)
}

fun handleSignIn(result: Credential, onGoogleSignIn: () -> Unit) {
    when (result) {
        is GoogleIdTokenCredential -> {
            if (result.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                try {

                    val googleIdTokenCredential = GoogleIdTokenCredential
                        .createFrom(result.data)
                    val firebaseCredential =
                        GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)
                    Firebase.auth.signInWithCredential(firebaseCredential)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d("GooGleSignIn", "signInWithCredential:success")
                                onGoogleSignIn()
                            } else {
                                Log.e(
                                    "GooGleSignIn",
                                    "signInWithCredential:failure",
                                    task.exception
                                )

                            }
                        }
                    //TODO DEPLACER DANS REPOSITORY ET ADD CREATION DE USER FIRESTORE
                } catch (e: GoogleIdTokenParsingException) {
                    Log.e("GooGleSignIn", "Received an invalid google id token response", e)
                }
            } else {
                Log.e("GooGleSignIn", "Unexpected type of credential")
            }
        }

        else -> {
            Log.e("GooGleSignIn", "Unexpected type of credential : 64${result.type}")
        }
    }
}

@Preview
@Composable
fun LogInProvidersPreview() {
    EventoriasTheme {
        LoginProviders(onMailClick = { }, onGoogleSignIn = { })
    }
}