package com.openclassrooms.eventorias.screen.recoveraccountwithmail

import android.app.AlertDialog
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.openclassrooms.eventorias.R
import com.openclassrooms.eventorias.screen.component.CustomTextField
import com.openclassrooms.eventorias.screen.component.RedButton
import com.openclassrooms.eventorias.ui.theme.EventoriasTheme
import org.koin.compose.viewmodel.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecoverAccountWithMailScreen(
    modifier: Modifier = Modifier,
    viewModel: RecoverAccountWithMailViewModel = koinViewModel(),
    onRecover: () -> Unit, mail: String, onError: () -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(id = R.string.log_In))
                }
            )
        }
    ) { innerPadding ->
        RecoverAccountWithMail(
            modifier = Modifier.padding(innerPadding),
            onRecover,
            mail,
            onError,
            viewModel::recoverAccountWithMail
        )
    }
}

@Composable
fun RecoverAccountWithMail(
    modifier: Modifier = Modifier,
    onRecover: () -> Unit,
    mail: String,
    onError: () -> Unit,
    recover: (String) -> Task<Void>
) {
    Column(
        modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        Text(
            text = stringResource(R.string.recover_account),
            color = MaterialTheme.colorScheme.secondary
        )
        var mailLocal by remember { mutableStateOf(mail) }


        CustomTextField(
            value = mailLocal,
            onValueChange = { mailLocal = it },
            label = "E-mail",
            modifier = Modifier.fillMaxWidth()
        )
        val recoveryPopupMessage =
            stringResource(R.string.popup_message_recovery_account, mailLocal)
        val context = LocalContext.current
        RedButton(
            text = stringResource(R.string.send),
            onClick = {
                recover(mailLocal).addOnSuccessListener {
                    openMailSendDialog(
                        context = context,
                        onRecover = onRecover,
                        stringMessage = recoveryPopupMessage
                    )
                }.addOnFailureListener {
                    onError()
                }
            },
            modifier = Modifier.align(Alignment.End)
        )
    }


    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_eventorias),
            contentDescription = "Eventorias logo"
        )

    }

}

private fun openMailSendDialog(
    context: Context,
    onRecover: () -> Unit,
    stringMessage: String
) {
    AlertDialog.Builder(context).setMessage(stringMessage)
        .setPositiveButton(
            "OK"
        ) { _, _ ->
            onRecover()
        }
        .show()
}

@Preview
@Composable
fun PreviewRecoverAccountWithMailScreen() {
    EventoriasTheme {
        RecoverAccountWithMail(
            onRecover = {},
            mail = "",
            onError = {},
            recover = { _ -> Tasks.forResult(null) }
        )
    }
}