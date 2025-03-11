package com.openclassrooms.eventorias.screen.login.loginwithpassword

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.AuthResult
import com.openclassrooms.eventorias.R
import com.openclassrooms.eventorias.screen.component.CustomPasswordTextField
import com.openclassrooms.eventorias.screen.component.CustomTextField
import com.openclassrooms.eventorias.screen.component.RedButton
import com.openclassrooms.eventorias.screen.component.WhiteButton
import com.openclassrooms.eventorias.screen.login.createaccountwithmail.FormError
import com.openclassrooms.eventorias.screen.login.createaccountwithmail.FormEvent
import com.openclassrooms.eventorias.ui.theme.EventoriasTheme
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginWithPasswordScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginWithPasswordViewModel = koinViewModel(),
    onLogin: () -> Unit,
    onRecoverClick: (String) -> Unit
) {
    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(0.dp),
                title = {
                    Text(stringResource(id = R.string.log_In))
                }
            )
        }
    ) { innerPadding ->
        val user by viewModel.user.collectAsStateWithLifecycle()
        val error by viewModel.error.collectAsStateWithLifecycle()
        LoginWithPassword(modifier = Modifier.padding(innerPadding), onLogin, onRecoverClick, viewModel::login,
            email = user.email,
            onEmailChanged = { viewModel.onAction(FormEvent.EmailChanged(it)) },error)

    }
}

@Composable
private fun LoginWithPassword(
    modifier: Modifier = Modifier,
    onLogInClick: () -> Unit,
    onRecoverClick: (String) -> Unit,
    login: (String, String) -> Task<AuthResult>,
    email: String,
    onEmailChanged: (String) -> Unit,
    error: FormError?
) {
    Column(
        modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        var passwordLocal by remember { mutableStateOf("") }

        Text(
            text = stringResource(R.string.log_in_with_password),
            color = MaterialTheme.colorScheme.secondary
        )

        CustomTextField(
            value = email,
            onValueChange = { onEmailChanged(it) },
            label = "E-mail",
            modifier = Modifier.fillMaxWidth()
        )

        if (error is FormError.EmailError) {
            Text(
                text = stringResource(id = error.messageRes),
                color = MaterialTheme.colorScheme.error,
            )
        }

        CustomPasswordTextField(
            value = passwordLocal,
            onValueChange = { passwordLocal = it },
            label = stringResource(R.string.password),
            modifier = Modifier.fillMaxWidth()
        )

        Row(modifier = Modifier.fillMaxWidth(),verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            WhiteButton(
                modifier = Modifier.width(200.dp),
                text = stringResource(R.string.cant_log_in),
                onClick = { onRecoverClick(email) }
            )
            val context = LocalContext.current
            RedButton(
                enabled = error == null && email.isNotEmpty() && passwordLocal.isNotEmpty(),
                text = stringResource(R.string.log_In),
                onClick = { login(email, passwordLocal).addOnSuccessListener {
                    onLogInClick()
                }.addOnFailureListener {
                    Toast.makeText(context,
                        context.getString(R.string.incorrect_password), Toast.LENGTH_SHORT).show()
                } },
            )

        }
    }



    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_eventorias),
            contentDescription = "Eventorias logo"
        )

    }

}

@Preview(locale = "en")
@Composable
fun PreviewLogInWithPassword() {
    EventoriasTheme {
        LoginWithPassword(
            onLogInClick = { },
            onRecoverClick = { _ -> },
            login = { _, _ -> Tasks.forResult(null) },
            email = "aaa",
            onEmailChanged = {},
            error = null
        )
    }
}