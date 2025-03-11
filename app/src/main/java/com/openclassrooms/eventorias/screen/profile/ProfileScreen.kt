package com.openclassrooms.eventorias.screen.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier, onSignOut: () -> Unit
) {
    Scaffold { contentPadding ->
        Profile(
            modifier = modifier.padding(contentPadding),
            onSignOut = onSignOut
        )

    }
}

@Composable
fun Profile(modifier: Modifier = Modifier, onSignOut: () -> Unit) {
    Column() {  }
}
