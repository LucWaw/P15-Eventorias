package com.openclassrooms.eventorias.screen.loginwithpassword

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.AuthResult
import com.openclassrooms.eventorias.R
import com.openclassrooms.eventorias.screen.component.CustomTextField
import com.openclassrooms.eventorias.screen.component.RedButton
import com.openclassrooms.eventorias.screen.component.WhiteButton
import com.openclassrooms.eventorias.ui.theme.EventoriasTheme
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginWithPasswordScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginWithPasswordViewModel = koinViewModel(),
    onLogin: () -> Unit,
    onRecoverClick: () -> Unit,
    onError: () -> Unit
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
        LoginWithPassword(modifier = Modifier.padding(innerPadding), onLogin, onRecoverClick, onError, viewModel::login)

    }
}

@Composable
fun LoginWithPassword(
    modifier: Modifier = Modifier,
    onLogInClick: () -> Unit,
    onRecoverClick: () -> Unit,
    onError: () -> Unit,
    login: (String, String) -> Task<AuthResult>
) {
    Column(
        modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        var emailLocal by remember { mutableStateOf("") }
        var passwordLocal by remember { mutableStateOf("") }

        Text(
            text = stringResource(R.string.log_in_with_password),
            color = MaterialTheme.colorScheme.secondary
        )

        CustomTextField(
            value = emailLocal,
            onValueChange = { emailLocal = it },
            label = "E-mail",
            modifier = Modifier.fillMaxWidth()
        )

        CustomTextField(
            value = passwordLocal,
            onValueChange = { passwordLocal = it },
            label = stringResource(R.string.password),
            modifier = Modifier.fillMaxWidth()
        )

        Row(modifier = Modifier.fillMaxWidth(),verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            WhiteButton(
                text = stringResource(R.string.cant_log_in),
                onClick = { onRecoverClick() }
            )

            RedButton(
                text = stringResource(R.string.log_In),
                onClick = { login(emailLocal, passwordLocal).addOnSuccessListener {
                    onLogInClick()
                }.addOnFailureListener {
                    onError()
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

@Preview
@Composable
fun PreviewLogInWithPassword() {
    EventoriasTheme {
        LoginWithPassword(
            onLogInClick = { },
            onRecoverClick = { },
            onError = { },
            login = { _, _ -> Tasks.forResult(null) }
        )
    }
}