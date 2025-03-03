package com.openclassrooms.eventorias.screen.login.loginproviders

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
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.firebase.auth.AuthResult
import com.openclassrooms.eventorias.BuildConfig
import com.openclassrooms.eventorias.R
import com.openclassrooms.eventorias.screen.component.ButtonWithEmailIcon
import com.openclassrooms.eventorias.screen.component.ButtonWithGoogleIcon
import com.openclassrooms.eventorias.ui.theme.EventoriasTheme
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginProvidersScreen(
    modifier: Modifier = Modifier,
    onMailClick: () -> Unit,
    viewModel: LoginProvidersViewModel = koinViewModel(),
    onGoogleSignIn: () -> Unit
) {
    Scaffold(modifier = modifier) { innerPadding ->
        LoginProviders(
            modifier = Modifier.padding(innerPadding),
            onMailClick,
            onGoogleSignIn,
            viewModel::loginWithGoogle
        )
    }

}

@Composable
private fun LoginProviders(
    modifier: Modifier = Modifier,
    onMailClick: () -> Unit,
    onGoogleSignIn: () -> Unit,
    loginWithGoogle: (Credential) -> Task<AuthResult>
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
                        signInWithGoogle(context, onGoogleSignIn, loginWithGoogle)
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

private suspend fun signInWithGoogle(
    context: Context,
    onGoogleSignIn: () -> Unit,
    loginWithGoogle: (Credential) -> Task<AuthResult>
) {

    val googleIdOption: GetSignInWithGoogleOption =
        GetSignInWithGoogleOption.Builder(BuildConfig.WEB_ID)
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

    loginWithGoogle(credential).addOnSuccessListener { result ->
        onGoogleSignIn()
    }.addOnFailureListener { e ->
        Log.e("GooGleSignIn", "Error signing in with Google", e)
    }
}


@Preview
@Composable
fun LogInProvidersPreview() {
    EventoriasTheme {
        LoginProviders(
            onMailClick = { },
            onGoogleSignIn = { },
            loginWithGoogle = { _ -> Tasks.forResult(null) }
        )
    }
}