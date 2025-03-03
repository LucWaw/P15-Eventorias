package com.openclassrooms.eventorias.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import com.openclassrooms.eventorias.screen.component.RedButton
import com.openclassrooms.eventorias.screen.component.WhiteButton
import com.openclassrooms.eventorias.ui.theme.EventoriasTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginMailSelectorScreen(
    modifier: Modifier = Modifier,
    onCreateAccountClick: () -> Unit,
    onLogInClick: () -> Unit
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
        LoginMailSelector(
            modifier = Modifier.padding(innerPadding),
            onCreateAccountClick = onCreateAccountClick,
            onLogInClick = onLogInClick
        )

    }
}

@Composable
private fun LoginMailSelector(
    modifier: Modifier = Modifier,
    onCreateAccountClick: () -> Unit,
    onLogInClick: () -> Unit
) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {

        Column(
            modifier
                .width(242.dp)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            WhiteButton(
                text = stringResource(R.string.already_have_an_account),
                modifier = Modifier.fillMaxWidth(),
                onClick = { onLogInClick() }
            )

            RedButton(
                text = stringResource(R.string.not_account),
                modifier = Modifier.fillMaxWidth(),
                onClick = { onCreateAccountClick() }
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
fun PreviewLogInWithMail() {
    EventoriasTheme {
        LoginMailSelector(
            onCreateAccountClick = {},
            onLogInClick = {}
        )
    }
}