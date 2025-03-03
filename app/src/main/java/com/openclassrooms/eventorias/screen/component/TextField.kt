package com.openclassrooms.eventorias.screen.component

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.openclassrooms.eventorias.R
import com.openclassrooms.eventorias.ui.theme.EventoriasTheme
import com.openclassrooms.eventorias.ui.theme.GreyLight
import com.openclassrooms.eventorias.ui.theme.GreySuperLight


@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    value: String,
    placeholder: String = "",
    onValueChange: (String) -> Unit,
    label: String,
) {
    TextField(
        modifier = modifier,
        shape = RoundedCornerShape(4.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.tertiary,
            unfocusedContainerColor = MaterialTheme.colorScheme.tertiary,
            disabledContainerColor = MaterialTheme.colorScheme.tertiary,
            unfocusedLabelColor = GreyLight,
            focusedLabelColor = GreyLight,
            disabledLabelColor = GreyLight,
            unfocusedTextColor = GreySuperLight,
            focusedTextColor = GreySuperLight,
            disabledTextColor = GreySuperLight,
            unfocusedPlaceholderColor = GreySuperLight,
            focusedPlaceholderColor = GreySuperLight,
            disabledPlaceholderColor = GreySuperLight,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
        ),
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        placeholder = { Text(placeholder) }
    )

}

@Composable
fun CustomPasswordTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    TextField(
        modifier = modifier,
        shape = RoundedCornerShape(4.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.tertiary,
            unfocusedContainerColor = MaterialTheme.colorScheme.tertiary,
            disabledContainerColor = MaterialTheme.colorScheme.tertiary,
            unfocusedLabelColor = GreyLight,
            focusedLabelColor = GreyLight,
            disabledLabelColor = GreyLight,
            unfocusedTextColor = GreySuperLight,
            focusedTextColor = GreySuperLight,
            disabledTextColor = GreySuperLight,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
        ),
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            val image = if (passwordVisible)
                painterResource(R.drawable.visibility)
            else painterResource(R.drawable.visibility_off)

            // Please provide localized description for accessibility services
            val description = if (passwordVisible) "Hide password" else "Show password"

            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(painter = image, description, tint = Color.White)
            }
        }
    )

}


@Preview
@Composable
fun CustomTextFieldPreview() {
    EventoriasTheme {
        CustomTextField(
            value = "John Doe",
            onValueChange = {},
            label = "Label"
        )
    }
}

@Preview
@Composable
fun CustomPasswordTextFieldPreview() {
    EventoriasTheme {
        CustomPasswordTextField(
            value = "John Doe",
            onValueChange = {},
            label = "Label"
        )
    }
}