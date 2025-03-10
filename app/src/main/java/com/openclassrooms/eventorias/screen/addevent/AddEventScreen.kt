package com.openclassrooms.eventorias.screen.addevent

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.openclassrooms.eventorias.R
import com.openclassrooms.eventorias.screen.component.CustomTextField
import com.openclassrooms.eventorias.screen.component.RedButton
import com.openclassrooms.eventorias.ui.theme.EventoriasTheme
import com.openclassrooms.eventorias.util.DateUtils.Companion.convertMillisToLocalDate
import com.openclassrooms.eventorias.util.DateUtils.Companion.toHumanDate
import com.openclassrooms.eventorias.util.TimeUtils.Companion.toHumanTime
import org.koin.compose.viewmodel.koinViewModel
import java.time.LocalDate
import java.time.LocalTime
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEventScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    viewModel: AddEventViewModel = koinViewModel()
) {
    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        modifier = modifier,
        topBar =
            {
                TopAppBar(
                    windowInsets = WindowInsets(0.dp),
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
        val post by viewModel.post.collectAsStateWithLifecycle()
        val error by viewModel.error.collectAsStateWithLifecycle()
        val uriImage by viewModel.uriImage.collectAsStateWithLifecycle()
        val pickMedia =
            rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                // Callback is invoked after the user selects a media item or closes the
                // photo picker.
                if (uri != null) {
                    Log.d("PhotoPicker", "Selected URI: $uri")
                    viewModel.onAction(AddPostFormEvent.ImageChanged(uri))
                } else {
                    Log.d("PhotoPicker", "No media selected")
                }
            }
        AddEvent(
            modifier = Modifier.padding(innerPadding),
            error = error,
            title = post.title,
            onTitleChanged = { viewModel.onAction(AddPostFormEvent.TitleChanged(it)) },
            description = post.description,
            onDescriptionChanged = { viewModel.onAction(AddPostFormEvent.DescriptionChanged(it)) },
            date = post.eventDate,
            onDateChanged = { viewModel.onAction(AddPostFormEvent.LocalDateChanged(it)) },
            time = post.eventHours,
            onTimeChanged = { viewModel.onAction(AddPostFormEvent.LocalTimeChanged(it)) },
            address = post.eventLocation,
            onAddressChanged = { viewModel.onAction(AddPostFormEvent.AddressChanged(it)) },
            onSaveClicked = {
                viewModel.addPost()?.addOnSuccessListener { onBackClick() }
            },
            openPhotoPicker = {
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            },
            uriImage = uriImage

        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddEvent(
    modifier: Modifier = Modifier,
    error: AddPostFormError?,
    title: String,
    onTitleChanged: (String) -> Unit,
    description: String,
    onDescriptionChanged: (String) -> Unit,
    date: LocalDate?,
    onDateChanged: (LocalDate) -> Unit,
    time: LocalTime?,
    onTimeChanged: (LocalTime) -> Unit,
    address: String,
    onAddressChanged: (String) -> Unit,
    onSaveClicked: () -> Unit,
    openPhotoPicker: () -> Unit,
    uriImage: Uri?
) {
    var descriptionIsFocused by remember { mutableStateOf(false) }

    var titleIsFocused by remember { mutableStateOf(false) }

    var addressIsFocused by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        CustomTextField(
            value = if (title.isEmpty() && !titleIsFocused) {
                stringResource(R.string.new_event)
            } else {
                title
            },
            onValueChange = { onTitleChanged(it) },
            label = stringResource(R.string.title),
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { titleIsFocused = it.isFocused }

        )
        CustomTextField(
            value = if (description.isEmpty() && !descriptionIsFocused) {
                stringResource(R.string.tap_here_to_enter_your_description)
            } else {
                description
            },
            onValueChange = { onDescriptionChanged(it) },
            label = "Description",
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { descriptionIsFocused = it.isFocused }

        )
        var showDialWithTimeDialog by remember { mutableStateOf(false) }
        val currentTime = Calendar.getInstance()
        val selectedTime = rememberTimePickerState(
            initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
            initialMinute = currentTime.get(Calendar.MINUTE),
            is24Hour = Locale.getDefault().language == "fr"
        )

        var showDialWithDateDialog by remember { mutableStateOf(false) }
        val selectedDate: DatePickerState = rememberDatePickerState()

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                CustomTextField(
                    value = date?.toHumanDate()
                        ?: stringResource(
                            R.string.date_format
                        ),
                    onValueChange = {},
                    label = "Date",
                    modifier = Modifier.fillMaxWidth()
                )
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .alpha(0f)
                        .clickable(onClick = {
                            showDialWithDateDialog = true
                        }),
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                CustomTextField(
                    value = time?.toHumanTime() ?: "HH : MM",
                    onValueChange = {},
                    label = stringResource(R.string.time),
                    modifier = Modifier.fillMaxWidth()
                )
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .alpha(0f)
                        .clickable(onClick = {
                            showDialWithTimeDialog = true
                        }),
                )

            }

        }


        if (showDialWithDateDialog) {

            DatePickerDialog(
                onDismissRequest = {
                    showDialWithDateDialog = false
                },
                confirmButton =
                    {
                        TextButton(onClick = {
                            onDateChanged(convertMillisToLocalDate(selectedDate.selectedDateMillis!!)) // !! because on confirm : sure that selectedDate.selectedDateMillis is not null
                            showDialWithDateDialog = false
                        }) {
                            Text("OK")
                        }
                    },

                ) {
                //All text Color in white
                DatePicker(
                    state = selectedDate,
                    colors = DatePickerDefaults.colors(
                        titleContentColor = Color.White,
                        headlineContentColor = Color.White,
                        weekdayContentColor = Color.White,
                        selectedDayContentColor = Color.White,
                        dayContentColor = Color.White,
                        yearContentColor = Color.White,
                        navigationContentColor = Color.White,
                    )
                )
            }

        }

        if (showDialWithTimeDialog) {
            DialWithTimeDialog(
                onDismiss = {
                    showDialWithTimeDialog = false
                },
                onConfirm = {
                    onTimeChanged(it)
                    showDialWithTimeDialog = false
                },
                timePickerState = selectedTime
            )
        }

        CustomTextField(
            value = if (address.isEmpty() && !addressIsFocused) {
                stringResource(R.string.enter_full_address)
            } else {
                address
            },
            onValueChange = { onAddressChanged(it) },
            label = stringResource(R.string.address),
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { addressIsFocused = it.isFocused }
        )

        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(
                space = 16.dp,
                alignment = Alignment.CenterHorizontally
            )
        ) {
            FloatingActionButton(
                containerColor = Color.White,
                onClick = { /*TODO*/ },
            ) {
                Icon(
                    painter = painterResource(R.drawable.icon_photo),
                    contentDescription = stringResource(R.string.take_a_picture),
                    tint = Color.Unspecified
                )
            }

            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.primary,
                onClick = { openPhotoPicker() },
            ) {
                Icon(
                    painter = painterResource(R.drawable.file_icon),
                    contentDescription = stringResource(R.string.select_a_picture),
                    tint = Color.Unspecified
                )
            }
        }

        AsyncImage(
            model = uriImage,
            contentDescription = stringResource(R.string.image_selected),
            modifier = Modifier
                .padding(4.dp)
                .width(500.dp)
                .heightIn(max = 200.dp)
                .align(Alignment.CenterHorizontally),
            contentScale = ContentScale.Crop,
        )


        RedButton(
            modifier = Modifier
                .fillMaxWidth(),
            enabled = error == null,
            onClick = { onSaveClicked() },
            text = stringResource(R.string.action_save)
        )


    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialWithTimeDialog(
    onConfirm: (LocalTime) -> Unit,
    timePickerState: TimePickerState,
    onDismiss: () -> Unit,
) {
    TimePickerDialog(
        onDismiss = { onDismiss() },
        onConfirm = {
            onConfirm(LocalTime.of(timePickerState.hour, timePickerState.minute))

        }
    ) {
        TimePicker(
            state = timePickerState,
        )
    }
}

@Composable
fun TimePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = { onConfirm() }) {
                Text("OK")
            }
        },
        text = { content() }
    )
}

@Preview
@Composable
fun PreviewAddEvent() {
    EventoriasTheme {
        AddEvent(
            error = null,
            title = "Title",
            onTitleChanged = {},
            description = "Description",
            onDescriptionChanged = {},
            date = LocalDate.now(),
            onDateChanged = {},
            time = LocalTime.now(),
            onTimeChanged = {},
            address = "Address",
            onAddressChanged = {},
            onSaveClicked = {},
            openPhotoPicker = {},
            uriImage = null
        )
    }
}