package com.openclassrooms.eventorias.screen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.openclassrooms.eventorias.R

@Composable
fun ErrorState(modifier: Modifier = Modifier, onTryAgain: () -> Unit) {

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier.fillMaxSize()) {
        Box(
            modifier = modifier
                .clip(
                    CircleShape
                )
                .size(64.dp)
                .background(MaterialTheme.colorScheme.onSurfaceVariant),
            contentAlignment = Alignment.Center,

            ) {
            Icon(
                painter = painterResource(id = R.drawable.icon_error),
                contentDescription = stringResource(R.string.error),
                tint = Color.White
            )
        }
        Text(

            text = stringResource(R.string.errorTitle),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
        )
        Text(
            modifier = Modifier.width(164.dp),
            text = stringResource(R.string.errorDescription),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White,
            textAlign = TextAlign.Center
        )
        RedButton(
            modifier = Modifier.padding(top = 35.dp),
            text = stringResource(R.string.retry),
            onClick = { onTryAgain() }
        )

    }
}