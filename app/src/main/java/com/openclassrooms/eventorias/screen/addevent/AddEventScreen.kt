package com.openclassrooms.eventorias.screen.addevent

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.openclassrooms.eventorias.R
import com.openclassrooms.eventorias.screen.component.CustomTextField
import com.openclassrooms.eventorias.ui.theme.EventoriasTheme
import com.openclassrooms.eventorias.util.DateUtils.Companion.convertMillisToLocalDate
import com.openclassrooms.eventorias.util.DateUtils.Companion.toHumanDate
import com.openclassrooms.eventorias.util.TimeUtils.Companion.toHumanTime
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import java.time.LocalTime
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEventScreen(modifier: Modifier = Modifier, onBackClick: () -> Unit) {
    Scaffold(
        modifier = modifier,
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

@OptIn(ExperimentalCoroutinesApi::class)
suspend fun getLatitudeAndLongitudeFromAddressName(
    context: Context,
    address: String
): Pair<Double, Double>? {
    val geocoder = Geocoder(context, Locale.getDefault())
    return suspendCancellableCoroutine { continuation ->
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            geocoder.getFromLocationName(
                address, 1,
                object : Geocoder.GeocodeListener {
                    override fun onGeocode(addresses: MutableList<Address>) {
                        if (addresses.isNotEmpty()) {
                            val firstAddress = addresses[0]
                            val latitude = firstAddress.latitude
                            val longitude = firstAddress.longitude
                            continuation.resume(Pair(latitude, longitude), onCancellation = null)
                        } else {
                            continuation.resume(null, onCancellation = null)
                        }
                    }

                    override fun onError(errorMessage: String?) {
                        super.onError(errorMessage)
                        continuation.resume(null, onCancellation = null)
                    }
                })
        } else {
            continuation.resume(null, onCancellation = null)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddEvent(modifier: Modifier = Modifier) {
    var description by remember { mutableStateOf("") }
    var descriptionisFocused by remember { mutableStateOf(false) }

    var title by remember { mutableStateOf("") }
    var titleisFocused by remember { mutableStateOf(false) }



    var address by remember { mutableStateOf("") }
    var addressisFocused by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        CustomTextField(
            value = if (title.isEmpty() && !titleisFocused) {
                stringResource(R.string.new_event)
            } else {
                title
            },
            onValueChange = { title = it },
            label = stringResource(R.string.title),
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { titleisFocused = it.isFocused }

        )
        CustomTextField(
            value = if (description.isEmpty() && !descriptionisFocused) {
                stringResource(R.string.tap_here_to_enter_your_description)
            } else {
                description
            },
            onValueChange = { description = it },
            label = "Description",
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { descriptionisFocused = it.isFocused }

        )
        var showDialWithTimeDialog by remember { mutableStateOf(false) }
        var selectedTime: TimePickerState? by remember { mutableStateOf(null) }

        var showDialWithDateDialog by remember { mutableStateOf(false) }
        val selectedDate: DatePickerState = rememberDatePickerState()

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                CustomTextField(
                    value = selectedDate.selectedDateMillis?.let { convertMillisToLocalDate(it).toHumanDate() } ?: stringResource(
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
                    value = selectedTime?.let { LocalTime.of(it.hour, it.minute).toHumanTime() } ?: "HH : MM",
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
        CustomTextField(
            value = if (address.isEmpty() && !addressisFocused) {
                stringResource(R.string.enter_full_address)
            } else {
                address
            },
            onValueChange = { address = it },
            label = stringResource(R.string.address),
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { addressisFocused = it.isFocused }

        )

        if (showDialWithDateDialog) {

            DatePickerDialog(
                onDismissRequest = {
                    showDialWithDateDialog = false
                },
                confirmButton =
                    {
                        TextButton(onClick = {
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
                onConfirm = { time ->
                    selectedTime = time
                    showDialWithTimeDialog = false
                },
            )
        }


    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialWithTimeDialog(
    onConfirm: (TimePickerState) -> Unit,
    onDismiss: () -> Unit,
) {
    val currentTime = Calendar.getInstance()

    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = Locale.getDefault().language == "fr"
    )

    TimePickerDialog(
        onDismiss = { onDismiss() },
        onConfirm = { onConfirm(timePickerState)

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
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("Dismiss")
            }
        },
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
        AddEvent()
    }
}