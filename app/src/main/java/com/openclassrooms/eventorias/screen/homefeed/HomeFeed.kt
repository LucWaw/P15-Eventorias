package com.openclassrooms.eventorias.screen.homefeed

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import coil.compose.AsyncImage
import coil.imageLoader
import coil.util.DebugLogger
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.openclassrooms.eventorias.R
import com.openclassrooms.eventorias.domain.Event
import com.openclassrooms.eventorias.domain.User
import com.openclassrooms.eventorias.extension.LocalDateExt.Companion.toHumanDate
import com.openclassrooms.eventorias.screen.component.RedButton
import com.openclassrooms.eventorias.screen.component.WhiteButton
import com.openclassrooms.eventorias.ui.theme.EventoriasTheme
import com.openclassrooms.eventorias.ui.theme.GreyDate
import com.openclassrooms.eventorias.ui.theme.GreySuperLight
import org.koin.compose.viewmodel.koinViewModel
import java.time.LocalDate
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeFeedScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeFeedViewModel = koinViewModel(),
) {
    val scope = rememberCoroutineScope()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val focusRequester = remember { FocusRequester() }

    Scaffold(modifier = modifier,
        topBar = {
            var showText by rememberSaveable { mutableStateOf(true) }
            var searchText by remember { mutableStateOf("") }

            TopAppBar(
                modifier = Modifier.padding(horizontal = 12.dp),
                title = {
                    if (showText) {
                        Text(stringResource(R.string.event_List))
                    } else {
                        BasicTextField(
                            value = searchText,
                            onValueChange = { text ->
                                searchText = text
                                viewModel.filterEvents(text)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequester),
                            textStyle = MaterialTheme.typography.titleLarge.copy(
                                color = Color.White
                            ),
                            cursorBrush = SolidColor(Color.White),
                        )
                        LaunchedEffect(Unit) {
                            focusRequester.requestFocus()
                        }
                    }
                },
                actions = {
                    IconButton(
                        onClick = { showText = !showText },
                    ) {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = stringResource(R.string.add_a_event),
                            tint = Color.White
                        )
                    }
                    IconButton(
                        onClick = {
                            viewModel.sortEvents()
                        },
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.icon___sort),
                            contentDescription = stringResource(R.string.sort_event),
                            tint = Color.White
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /*TODO*/ },
            ) {
                Icon(Icons.Filled.Add, stringResource(R.string.add_a_event))
            }
        }
    ) { innerPadding ->



        if (state.isLoading) {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (state.isError) {
            ErrorState(
                modifier = modifier.padding(innerPadding),
                onTryAgain = { scope.launch {
                    viewModel.loadEvent().collect{}
                } }
            )
        } else {

            HomeFeed(modifier = Modifier.padding(innerPadding), state.event)
            WhiteButton(
                modifier = Modifier
                    .padding(24.dp),
                text = "Logout",
                onClick = { Firebase.auth.signOut() }
            )
        }
    }
}

@Composable
fun HomeFeed(modifier: Modifier = Modifier, items: List<Event>) {
    LazyColumn(
        modifier = modifier.padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
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
        modifier = modifier
            .height(80.dp)
            .fillMaxWidth(),
        onClick = { /* TODO */ }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = modifier.padding(horizontal = 16.dp, vertical = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (!event.author.urlPicture.isNullOrEmpty()) {
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
                }

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = if (event.title.length < 20) event.title else event.title.substring(
                            0,
                            17
                        ) + "...",
                        style = MaterialTheme.typography.bodyLarge,
                        color = GreySuperLight
                    )
                    Text(
                        text = event.eventDate.toHumanDate(),
                        color = GreyDate,

                        )

                }
            }
            Box(
                modifier = modifier
                    .fillMaxHeight()
                    .width(136.dp)
                    .clip(MaterialTheme.shapes.medium),
            ) {
                if (!event.photoUrl.isNullOrEmpty()) {
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxSize(),
                        model = event.photoUrl,
                        imageLoader = LocalContext.current.imageLoader.newBuilder()
                            .logger(DebugLogger())
                            .build(),
                        placeholder = ColorPainter(Color.White),
                        contentDescription = "image",
                        contentScale = ContentScale.Crop,
                    )
                }
            }
        }


    }

}


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

@Composable
@Preview
fun ErrorStatePreview() {
    EventoriasTheme {
        Box(Modifier.background(Color.Black)) {
            ErrorState(
                onTryAgain = { }
            )
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
