package com.openclassrooms.eventorias.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.openclassrooms.eventorias.R
import com.openclassrooms.eventorias.screen.component.CustomTextField
import com.openclassrooms.eventorias.screen.component.RedButton
import com.openclassrooms.eventorias.ui.theme.EventoriasTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAccountWithMailScreen(modifier: Modifier = Modifier, onLogin: () -> Unit) {
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
        CreateAccountWithMail(modifier = Modifier.padding(innerPadding), onLogin)

    }
}

@Composable
fun CreateAccountWithMail(modifier: Modifier = Modifier, onLogin: () -> Unit) {
    Column(
        modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        CustomTextField(
            value = "",
            onValueChange = {},
            label = "E-mail",
            modifier = Modifier.fillMaxWidth()
        )
        CustomTextField(
            value = "",
            onValueChange = {},
            label = stringResource(R.string.first_and_last_name),
            modifier = Modifier.fillMaxWidth()
        )

        CustomTextField(
            value = "",
            onValueChange = {},
            label = stringResource(R.string.password),
            modifier = Modifier.fillMaxWidth()
        )
        RedButton(
            text = stringResource(R.string.create_account),
            onClick = { /*TODO*/ },
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
        CreateAccountWithMail(onLogin = {  })
    }
}