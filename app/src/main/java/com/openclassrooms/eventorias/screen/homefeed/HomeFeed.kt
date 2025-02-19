package com.openclassrooms.eventorias.screen.homefeed

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.imageLoader
import coil.util.DebugLogger
import com.openclassrooms.eventorias.domain.Event
import com.openclassrooms.eventorias.domain.User
import com.openclassrooms.eventorias.extension.LocalDateExt.Companion.toHumanDate
import com.openclassrooms.eventorias.ui.theme.EventoriasTheme
import com.openclassrooms.eventorias.ui.theme.GreyDate
import com.openclassrooms.eventorias.ui.theme.GreySuperLight
import org.koin.compose.viewmodel.koinViewModel
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun HomeFeedScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeFeedViewModel = koinViewModel(),
) {
    val events by viewModel.events.collectAsStateWithLifecycle()

    Scaffold(modifier = modifier) { innerPadding ->
        HomeFeed(modifier = Modifier.padding(innerPadding), events)
    }
}

@Composable
fun HomeFeed(modifier: Modifier = Modifier, items: List<Event>) {
    LazyColumn(modifier = modifier) {
        items(items) { event ->
            EventCell(event = event)
        }
    }
}

@Composable
fun EventCell(
    modifier: Modifier = Modifier,
    event: Event
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onSurfaceVariant,
        ),
        modifier = modifier,
        onClick = { /* TODO */ }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape),
                model = event.author.urlPicture,
                imageLoader = LocalContext.current.imageLoader.newBuilder()
                    .logger(DebugLogger())
                    .build(),
                placeholder = ColorPainter(Color.White),
                contentDescription = "image",
                contentScale = ContentScale.Crop,
            )
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = event.title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = GreySuperLight
                )
                Text(
                    text = event.eventDate.toHumanDate(),
                    color = GreyDate,

                    )

            }
        }
    }

}


@Composable
@Preview
fun EventCellPreview() {
    EventoriasTheme {
        EventCell(
            event = Event(
                "5",
                "The Secret of the Flowers",
                "Improve your goldfish's physical fitness by getting him a bicycle.",
                LocalDate.of(2021, 8, 25),
                LocalTime.of(10, 0),
                "Location 1",
                "https://picsum.photos/id/80/1080/",
                User("1", "Gerry", "https://picsum.photos/id/80/1080/"),
            )
        )
    }
}
