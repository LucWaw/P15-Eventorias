package com.openclassrooms.eventorias.screen.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.imageLoader
import coil.util.DebugLogger
import com.openclassrooms.eventorias.BuildConfig
import com.openclassrooms.eventorias.R
import com.openclassrooms.eventorias.domain.Event
import com.openclassrooms.eventorias.domain.User
import com.openclassrooms.eventorias.ui.theme.EventoriasTheme
import com.openclassrooms.eventorias.util.DateUtils.Companion.toHumanDate
import com.openclassrooms.eventorias.util.TimeUtils.Companion.toHumanTime
import org.koin.compose.viewmodel.koinViewModel
import java.time.LocalDate
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    modifier: Modifier = Modifier, eventId: String,
    viewModel: DetailViewModel = koinViewModel(),
    onBackClick: () -> Boolean
) {
    val event by viewModel.event.collectAsStateWithLifecycle()
    LaunchedEffect(eventId) {
        viewModel.loadEvent(eventId)

    }
    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        modifier = modifier,
        topBar =
            {
                TopAppBar(
                    windowInsets = WindowInsets(0.dp),
                    title = {
                        Text(event.title)
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
                    },
                    actions = {
                        IconButton(
                            onClick = { viewModel.deleteEvent(eventId) },
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .testTag("deleteEvent")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                )
            }
    ) { innerPadding ->

        Detail(event = event, modifier = Modifier.padding(innerPadding))
    }

}

@Composable
private fun Detail(event: Event, modifier: Modifier = Modifier) {
    val scroll = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .verticalScroll(scroll),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        if (!event.photoUrl.isNullOrEmpty()) {
            AsyncImage(
                modifier = Modifier
                    .size(400.dp)
                    .align(Alignment.CenterHorizontally)
                    .clip(MaterialTheme.shapes.medium),
                model = event.photoUrl,
                imageLoader = LocalContext.current.imageLoader.newBuilder()
                    .logger(DebugLogger())
                    .build(),
                placeholder = ColorPainter(Color.DarkGray),
                contentDescription = "image",
                contentScale = ContentScale.Crop,
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.date_icon),
                        contentDescription = "date icon",
                    )
                    event.eventDate?.let { Text(text = it.toHumanDate()) }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_hours),
                        contentDescription = "hours icon",
                    )
                    event.eventHours?.let { Text(text = it.toHumanTime()) }
                }
            }
            if (!event.author.urlPicture.isNullOrEmpty()) {
                AsyncImage(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape),
                    model = event.author.urlPicture,
                    imageLoader = LocalContext.current.imageLoader.newBuilder()
                        .logger(DebugLogger())
                        .build(),
                    placeholder = ColorPainter(Color.DarkGray),
                    contentDescription = "image",
                    contentScale = ContentScale.Crop,
                )
            }
        }
        Text(text = event.description)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = event.eventLocation, modifier = Modifier.width(167.dp))

            AsyncImage(
                modifier = Modifier
                    .width(149.dp)
                    .height(72.dp)
                    .clip(RoundedCornerShape(12.dp)),
                model = "https://maps.googleapis.com/maps/api/staticmap?language=en&center=${event.latitude},${event.longitude}&zoom=18&size=600x300&maptype=roadmap&markers=color:blue%7Clabel:S%7C40.702147,-74.015794&markers=color:green%7Clabel:G%7C40.711614,-74.012318&markers=color:red%7Clabel:C%7C40.718217,-73.998284&key=${BuildConfig.MAPS_API_KEY}",
                imageLoader = LocalContext.current.imageLoader.newBuilder()
                    .logger(DebugLogger())
                    .build(),
                placeholder = ColorPainter(Color.DarkGray),
                contentDescription = "Maps from the location",
                contentScale = ContentScale.Crop,
            )
        }
    }
}


@Preview
@Composable
fun DetailScreenPreview() {
    EventoriasTheme {
        Detail(
            event = Event(
                "5",
                "The Secret of the Flowers",
                "Improve your goldfish's physical fitness by getting him a bicycle.",
                LocalDate.of(2021, 1, 25),
                LocalTime.of(10, 0),
                "Location 1",
                48.8566,
                2.3522,
                "https://picsum.photos/id/80/1080/",
                User("1", "Gerry", "https://picsum.photos/id/80/1080/"),
            )
        )
    }
}
