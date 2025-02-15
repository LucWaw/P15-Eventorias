package com.openclassrooms.eventorias.screen.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.openclassrooms.eventorias.R
import com.openclassrooms.eventorias.ui.theme.EventoriasTheme

@Composable
fun RedButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.secondary,
        ),
        shape = RoundedCornerShape(4.dp)
    ) {
        Text(text)
    }
}

@Composable
fun WhiteButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.tertiary,
        ),
        shape = RoundedCornerShape(4.dp)
    ) {
        Text(text)
    }
}

@Composable
fun ButtonWithGoogleIcon(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.tertiary,
        ),
        shape = RoundedCornerShape(4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Icon(
                painterResource(id = R.drawable.google_icon),
                tint = Color.Unspecified,
                contentDescription = null
            )
            Text(
                stringResource(
                    R.string.google_sign_in
                )
            )
        }

    }
}

@Composable
fun ButtonWithEmailIcon(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.secondary,
        ),
        shape = RoundedCornerShape(4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Email,
                contentDescription = null
            )
            Text(
                stringResource(
                    R.string.email_sign_in
                )
            )
        }
    }
}


@Preview
@Composable
fun RedButtonPreview() {
    EventoriasTheme {
        RedButton(onClick = {}, text = "Se connecter")
    }
}

@Preview
@Composable
fun WhiteButtonPreview() {
    EventoriasTheme {
        WhiteButton(onClick = {}, text = "Se connecter")
    }
}

@Preview(locale = "fr")
@Composable
fun ButtonWithGoogleIconPreview() {
    EventoriasTheme {
        ButtonWithGoogleIcon(onClick = {})
    }
}

@Preview(locale = "fr")
@Composable
fun ButtonWithEmailIconPreview() {
    EventoriasTheme {
        ButtonWithEmailIcon(onClick = {})
    }
}