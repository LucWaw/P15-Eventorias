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
fun LogInProvidersScreen(modifier: Modifier = Modifier) {

    Scaffold(modifier = modifier) { innerPadding ->
        LogInProviders(modifier = Modifier.padding(innerPadding))
    }

}

@Composable
fun LogInProviders(modifier: Modifier = Modifier) {
    Box (modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = modifier.width(242.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
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
                onClick = { /*TODO*/ }
            )
        }
    }

}

@Preview
@Composable
fun LogInProvidersPreview() {
    EventoriasTheme {
        LogInProviders()
    }
}