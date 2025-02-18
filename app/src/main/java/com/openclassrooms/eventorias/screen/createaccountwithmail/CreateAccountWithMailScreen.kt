package com.openclassrooms.eventorias.screen.createaccountwithmail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.AuthResult
import com.openclassrooms.eventorias.R
import com.openclassrooms.eventorias.screen.component.CustomTextField
import com.openclassrooms.eventorias.screen.component.RedButton
import com.openclassrooms.eventorias.ui.theme.EventoriasTheme
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAccountWithMailScreen(
    viewModel: CreateAccountWithMailViewModel = koinViewModel(),
    modifier: Modifier = Modifier, onLogin: () -> Unit, onError: () -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(id = R.string.log_In))
                }
            )
        }
    ) { innerPadding ->
        val user by viewModel.user.collectAsStateWithLifecycle()
        val error by viewModel.error.collectAsStateWithLifecycle()

        CreateAccountWithMail(
            modifier = Modifier.padding(innerPadding), onLogin, viewModel::createAccount, onError,
            email = user.email,
            onEmailChanged = { viewModel.onAction(FormEvent.EmailChanged(it)) },
            name = user.name,
            onNameChanged = { viewModel.onAction(FormEvent.NameChanged(it)) },
            password = user.password,
            onPasswordChanged = { viewModel.onAction(FormEvent.PasswordChanged(it)) },
            error
        )

    }
}

@Composable
fun CreateAccountWithMail(
    modifier: Modifier = Modifier,
    onLogin: () -> Unit,
    createAccount: (String, String, String) -> Task<AuthResult>, onError: () -> Unit,
    email: String,
    onEmailChanged: (String) -> Unit,
    name: String,
    onNameChanged: (String) -> Unit,
    password: String,
    onPasswordChanged: (String) -> Unit,
    error: FormError?
) {

    Column(
        modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
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


        CustomTextField(
            value = name,
            onValueChange = { onNameChanged(it) },
            label = stringResource(R.string.first_and_last_name),
            modifier = Modifier.fillMaxWidth()
        )
        if (error is FormError.NameError) {
            Text(
                text = stringResource(id = error.messageRes),
                color = MaterialTheme.colorScheme.error,
            )
        }

        CustomTextField(
            value = password,
            onValueChange = { onPasswordChanged(it) },
            label = stringResource(R.string.password),
            modifier = Modifier.fillMaxWidth()
        )
        if (error is FormError.PasswordError) {
            Text(
                text = stringResource(id = error.messageRes),
                color = MaterialTheme.colorScheme.error,
            )
        }

        RedButton(
            enabled = error == null,
            text = stringResource(R.string.create_account),
            onClick = {
                createAccount(email, password, name).addOnSuccessListener {
                    onLogin()
                }.addOnFailureListener {
                    onError()
                }
            },
            modifier = Modifier.align(Alignment.End)
        )

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

@Preview
@Composable
fun PreviewCreateAccountWithMail() {
    EventoriasTheme {
        CreateAccountWithMail(
            onLogin = { },
            createAccount = { _, _, _ -> Tasks.forResult(null) },
            onError = { },
            email = "",
            onEmailChanged = { },
            name = "",
            onNameChanged = { },
            password = "",
            onPasswordChanged = { },
            error = null
        )
    }
}