package com.openclassrooms.eventorias.screen.profile

import android.app.AlertDialog
import android.content.Context
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap.Companion.Round
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.imageLoader
import coil.util.DebugLogger
import com.google.android.gms.tasks.Task
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

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(0.dp),
                title = {
                    Text(stringResource(R.string.user_profile))
                },
                actions = {
                    if (!state.user.urlPicture.isNullOrEmpty()) {
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
                            contentDescription = "image",
                            contentScale = ContentScale.Crop,
                        )
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


    }
}


@Composable
fun Profile(
    modifier: Modifier = Modifier,
    onSignOut: () -> Unit,
    user: User,
    onDeleteAccount: () -> Unit
) {
    Column(
        modifier = modifier.padding(horizontal = 24.dp, vertical = 22.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        UserData(user)

        Spacer(modifier = Modifier.height(66.dp))

        AccountHandlingButtons(
            onSignOut = { onSignOut() },
            onDeleteAccount = { onDeleteAccount() }
        )
    }
}

private fun openDeleteDialog(
    deleteUser: () -> Task<Void>,
    onBackClick: () -> Unit,
    context: Context
) {
    AlertDialog.Builder(context)
        .setMessage(R.string.popup_message_confirmation_delete_account)
        .setPositiveButton(
            R.string.popup_message_choice_yes
        ) { _, _ ->
            deleteUser()
                .addOnSuccessListener {
                    onBackClick()
                }
        }
        .setNegativeButton(R.string.popup_message_choice_no, null)
        .show()
}


@Composable
private fun UserData(user: User) {
    CustomTextField(
        value = user.displayName,
        label = "Name",
        enabled = false,
        onValueChange = { },
        modifier = Modifier.fillMaxWidth()
    )

    CustomTextField(
        value = user.email,
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
            onDeleteAccount = {}
        )
    }
}