package com.openclassrooms.eventorias.screen.addevent

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.openclassrooms.eventorias.R
import com.openclassrooms.eventorias.ui.theme.EventoriasTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEventScreen(modifier: Modifier = Modifier, onBackClick: () -> Unit) {
    Scaffold(
        topBar =
        {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.creation_of_an_event))
                },
                navigationIcon = {
                    IconButton(onClick = {
                        onBackClick()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.contentDescription_go_back)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        AddEvent(modifier = Modifier.padding(innerPadding))
    }
}

@Composable
fun AddEvent(modifier: Modifier = Modifier) {
    Column {


    }
}


@Preview
@Composable
fun PreviewAddEvent() {
    EventoriasTheme {
        AddEvent()
    }
}