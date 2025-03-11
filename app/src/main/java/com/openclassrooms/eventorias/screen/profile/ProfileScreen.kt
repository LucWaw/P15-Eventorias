package com.openclassrooms.eventorias.screen.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.imageLoader
import coil.util.DebugLogger
import com.google.firebase.auth.FirebaseAuth
import com.openclassrooms.eventorias.R
import com.openclassrooms.eventorias.screen.component.CustomTextField
import com.openclassrooms.eventorias.screen.component.RedButton
import com.openclassrooms.eventorias.screen.component.WhiteButton
import com.openclassrooms.eventorias.ui.theme.EventoriasTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier, onSignOut: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.user_profile))
                },
                actions = {
                    AsyncImage(
                        modifier = Modifier
                            .padding(24.dp)
                            .size(48.dp)
                            .clip(CircleShape),
                        model = "https://xsgames.co/randomusers/assets/avatars/male/1.jpg", //TODO replace with user image
                        imageLoader = LocalContext.current.imageLoader.newBuilder()
                            .logger(DebugLogger())
                            .build(),
                        placeholder = ColorPainter(Color.LightGray),
                        contentDescription = "image",
                        contentScale = ContentScale.Crop,
                    )
                }
            )
        }
    ) { contentPadding ->
        Profile(
            modifier = modifier.padding(contentPadding),
            onSignOut = onSignOut
        )

    }
}

@Composable
fun Profile(modifier: Modifier = Modifier, onSignOut: () -> Unit) {
    Column(
        modifier = modifier.padding(horizontal = 24.dp, vertical = 22.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        CustomTextField(
            value = "",
            label = "Name",
            enabled = false,
            onValueChange = { },
            modifier = Modifier.fillMaxWidth()
        )

        CustomTextField(
            value = "",
            label = "E-mail",
            enabled = false,
            onValueChange = { },
            modifier = Modifier.fillMaxWidth()
        )
        var checked by remember { mutableStateOf(true) }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Switch(
                checked = checked,
                onCheckedChange = {
                    checked = it
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    uncheckedThumbColor = Color.White,
                ),

                )
            Text("Notifications")
        }

        Spacer(modifier = Modifier.height(66.dp))

        WhiteButton(
            text = stringResource(R.string.sign_out),
            onClick = {
                FirebaseAuth.getInstance().signOut() //TODO use ViewModel and repository
                onSignOut()
            },
            modifier = Modifier
                .width(242.dp)
                .height(52.dp)
                .align(Alignment.CenterHorizontally)
        )

        RedButton(
            text = stringResource(R.string.delete_account),
            onClick = { /*TODO*/ },
            modifier = Modifier
                .width(242.dp)
                .height(52.dp)
                .align(Alignment.CenterHorizontally)
        )
    }
}

@Preview
@Composable
fun ProfilePreview() {
    EventoriasTheme {
        Profile(onSignOut = {})
    }
}