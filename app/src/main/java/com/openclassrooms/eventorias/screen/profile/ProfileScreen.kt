package com.openclassrooms.eventorias.screen.profile

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap.Companion.Round
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.imageLoader
import coil.util.DebugLogger
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.openclassrooms.eventorias.R
import com.openclassrooms.eventorias.domain.User
import com.openclassrooms.eventorias.screen.component.CustomTextField
import com.openclassrooms.eventorias.screen.component.ErrorState
import com.openclassrooms.eventorias.screen.component.RedButton
import com.openclassrooms.eventorias.screen.component.WhiteButton
import com.openclassrooms.eventorias.ui.theme.EventoriasTheme
import com.openclassrooms.eventorias.ui.theme.GreyCircular
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = koinViewModel(),
    onSignOut: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    LaunchedEffect(
        Unit
    ) {
        viewModel.updateFirestoreMail()
    }
    val pickMedia =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            // Callback is invoked after the user selects a media item or closes the
            // photo picker.
            if (uri != null) {
                Log.d("PhotoPicker", "Selected URI: $uri")
                viewModel.onAction(ProfileAction.ImageChanged(uri))
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(0.dp),
                title = {
                    Text(stringResource(R.string.user_profile))
                },
                actions = {

                    if (state.user.googleSignIn) {
                        AsyncImage(
                            modifier = Modifier
                                .padding(24.dp)
                                .size(48.dp)
                                .clip(CircleShape),
                            model = state.user.urlPicture,
                            imageLoader = LocalContext.current.imageLoader.newBuilder()
                                .logger(DebugLogger())
                                .build(),
                            placeholder = ColorPainter(Color.LightGray),
                            contentDescription = "user image",
                            contentScale = ContentScale.Crop,
                        )
                    } else {
                        IconButton(
                            modifier = Modifier.padding(24.dp),
                            onClick = {
                                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                            },
                        ) {
                            if (state.user.urlPicture.isNullOrEmpty() && state.changedUri == Uri.EMPTY) {
                                Image(
                                    painter = painterResource(id = R.drawable.image_placeholder),
                                    contentDescription = "Default Profile",
                                    modifier = Modifier
                                        .fillMaxSize()
                                )
                            } else {
                                if (state.changedUri != Uri.EMPTY) {
                                    AsyncImage(
                                        modifier = Modifier
                                            .fillMaxSize(),
                                        model = state.changedUri,
                                        imageLoader = LocalContext.current.imageLoader.newBuilder()
                                            .logger(DebugLogger())
                                            .build(),
                                        placeholder = ColorPainter(Color.LightGray),
                                        contentDescription = "User Profile Image",
                                        contentScale = ContentScale.Crop,
                                    )
                                } else {
                                    AsyncImage(
                                        modifier = Modifier
                                            .fillMaxSize(),
                                        model = state.user.urlPicture,
                                        imageLoader = LocalContext.current.imageLoader.newBuilder()
                                            .logger(DebugLogger())
                                            .build(),
                                        placeholder = ColorPainter(Color.LightGray),
                                        contentDescription = "User Profile Image",
                                        contentScale = ContentScale.Crop,
                                    )
                                }
                            }

                        }
                    }

                }
            )
        }
    ) { contentPadding ->

        val context = LocalContext.current

        if (state.isLoading) {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    strokeWidth = 4.dp,
                    color = Color.White,
                    trackColor = GreyCircular,
                    strokeCap = Round,
                    gapSize = 4.dp
                )
            }
        } else if (state.isError) {
            Column(
                modifier = modifier
                    .padding(contentPadding)
                    .padding(horizontal = 24.dp, vertical = 22.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {

                Box(
                    modifier = Modifier.height(250.dp)
                ) {
                    ErrorState(
                        onTryAgain = {
                            scope.launch {
                                viewModel.loadUserData().collect {}
                            }
                        }
                    )
                }
                AccountHandlingButtons(
                    onSignOut = { viewModel::logout; onSignOut() },
                    onDeleteAccount = {
                        openDeleteDialog(
                            viewModel::deleteCurrentUser,
                            onSignOut,
                            context
                        )
                    }
                )
            }

        } else {

            Profile(
                modifier = modifier.padding(contentPadding),
                user = state.user,
                onSignOut = {
                    viewModel.logout()
                    onSignOut()
                },
                onDeleteAccount = {
                    openDeleteDialog(
                        viewModel::deleteCurrentUser,
                        onSignOut,
                        context
                    )
                },
                notification = state.notification,
                onCheckSwitch = { viewModel.onNotificationClicked(it) },
                onEmailChange = { viewModel.onAction(ProfileAction.EmailChanged(it)) },
                onNameChange = { viewModel.onAction(ProfileAction.NameChanged(it)) },
                onSaveUserInfo = { viewModel.updateProfileInfo() }
            )
        }


    }
}


@Composable
fun Profile(
    modifier: Modifier = Modifier,
    onSignOut: () -> Unit,
    user: User,
    notification: Boolean,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onCheckSwitch: (Boolean) -> Unit,
    onSaveUserInfo: () -> Task<Void>,
    onDeleteAccount: () -> Unit
) {
    Column(
        modifier = modifier.padding(horizontal = 24.dp, vertical = 22.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        UserData(
            user,
            notification = notification,
            onCheckSwitch = onCheckSwitch,
            onNameChange = onNameChange,
            onEmailChange = onEmailChange
        )

        Spacer(modifier = Modifier.height(66.dp))

        val context = LocalContext.current
        WhiteButton(
            text = stringResource(R.string.update_info),
            onClick = {
                onSaveUserInfo().addOnSuccessListener {
                    Toast.makeText(
                        context,
                        context.getString(R.string.saved), Toast.LENGTH_LONG
                    ).show()
                }.addOnFailureListener {
                    Toast.makeText(
                        context,
                        context.getString(R.string.error_update), Toast.LENGTH_LONG
                    ).show()
                }
            },
            modifier = Modifier
                .width(242.dp)
                .height(52.dp)
                .align(Alignment.CenterHorizontally)
        )

        AccountHandlingButtons(
            onSignOut = { onSignOut() },
            onDeleteAccount = { onDeleteAccount() }
        )
    }
}

private fun openDeleteDialog(
    deleteUser: () -> Task<Task<Void?>?>,
    onBackClick: () -> Unit,
    context: Context
) {
    AlertDialog.Builder(context)
        .setMessage(R.string.popup_message_confirmation_delete_account)
        .setPositiveButton(
            R.string.popup_message_choice_yes
        ) { _, _ ->
            deleteUser()
                .addOnSuccessListener { innerTask ->
                    innerTask?.addOnSuccessListener {
                        onBackClick()
                    }?.addOnFailureListener {
                        Toast.makeText(
                            context,
                            context.getString(R.string.delete_error_auth), Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(
                        context,
                        context.getString(R.string.delete_error_database), Toast.LENGTH_SHORT
                    ).show()
                }

        }
        .setNegativeButton(R.string.popup_message_choice_no, null)
        .show()
}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun UserData(
    user: User,
    modifier: Modifier = Modifier,
    notification: Boolean,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onCheckSwitch: (Boolean) -> Unit
) {
    val notificationsPermissionState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberPermissionState(
            Manifest.permission.POST_NOTIFICATIONS
        )
    } else {
        null
    }


    CustomTextField(
        value = user.displayName,
        label = "Name",
        onValueChange = { onNameChange(it) },
        modifier = Modifier.fillMaxWidth()
    )

    Log.d("ProfileScreen", "Is google signIn: ${user.googleSignIn}")

    CustomTextField(
        value = user.email,
        label = "E-mail",
        enabled = !user.googleSignIn,
        onValueChange = { onEmailChange(it) },
        modifier = Modifier.fillMaxWidth()
    )

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Switch(
            checked = notification,
            onCheckedChange = { isChecked ->
                onCheckSwitch(isChecked)
                if (isChecked == true && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (notificationsPermissionState?.status?.isGranted == false) {
                        notificationsPermissionState.launchPermissionRequest()
                    }
                }
            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                uncheckedThumbColor = Color.White,
            )
        )
        Text("Notifications")
    }
}

@Composable
private fun ColumnScope.AccountHandlingButtons(onSignOut: () -> Unit, onDeleteAccount: () -> Unit) {

    WhiteButton(
        text = stringResource(R.string.sign_out),
        onClick = {
            onSignOut()
        },
        modifier = Modifier
            .width(242.dp)
            .height(52.dp)
            .align(Alignment.CenterHorizontally)
    )

    RedButton(
        text = stringResource(R.string.delete_account),
        onClick = { onDeleteAccount() },
        modifier = Modifier
            .width(242.dp)
            .height(52.dp)
            .align(Alignment.CenterHorizontally)
    )
}

@Preview
@Composable
fun ProfilePreview() {
    EventoriasTheme {
        Profile(
            onSignOut = {}, user = User("1", "John Doe", ""),
            onDeleteAccount = {},
            notification = true,
            onCheckSwitch = { },
            onNameChange = { },
            onEmailChange = { },
            onSaveUserInfo = { Tasks.forResult(null) }
        )
    }
}