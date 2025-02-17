package com.openclassrooms.eventorias.screen

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.openclassrooms.eventorias.R
import com.openclassrooms.eventorias.screen.component.ButtonWithEmailIcon
import com.openclassrooms.eventorias.screen.component.ButtonWithGoogleIcon
import com.openclassrooms.eventorias.ui.theme.EventoriasTheme

@Composable
fun LoginProvidersScreen(modifier: Modifier = Modifier, onMailClick :()-> Unit) {

    Scaffold(modifier = modifier) { innerPadding ->
        LoginProviders(modifier = Modifier.padding(innerPadding), onMailClick)
    }

}

@Composable
fun LoginProviders(modifier: Modifier = Modifier, onMailClick: () -> Unit) {
    Box (modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
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
            ButtonWithGoogleIcon(
                modifier = Modifier.fillMaxWidth(),
                onClick = { /*TODO*/ }
            )
            ButtonWithEmailIcon(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onMailClick() }
            )
        }
    }

}

@Preview
@Composable
fun LogInProvidersPreview() {
    EventoriasTheme {
        LoginProviders(onMailClick = { })
    }
}