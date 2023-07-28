package ru.shtykin.testappchat.presentation.screen.registration

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import ru.shtykin.testappchat.presentation.screen.common_parts.VerticalSpace
import ru.shtykin.testappchat.presentation.state.ScreenState
import ru.shtykin.testappchat.presentation.utils.PhoneNumberVisualTransformation


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(
    uiState: ScreenState,
    onRegistrationClick: ((String, String, String) -> Unit)?,
    onBackClick: ((String) -> Unit)?,
) {

    val phone = (uiState as? ScreenState.RegistrationScreen)?.phone
    val errorMessage = (uiState as? ScreenState.RegistrationScreen)?.error
    var name by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var error: String? by remember { mutableStateOf(null) }

    val usernamePattern = remember { Regex("[a-zA-z-0123456789\\s]*") }


    SideEffect {
        error = errorMessage
    }
    BackHandler { onBackClick?.invoke(phone ?: "") }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.padding(32.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            VerticalSpace(100.dp)
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = phone ?: "",
                onValueChange = {},
                label = { Text("Номер телефона") },
                singleLine = true,
                readOnly = true,
                visualTransformation = PhoneNumberVisualTransformation(),
            )
            VerticalSpace(16.dp)
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = name,
                onValueChange = {
                    name = it
                    error = null
                },
                label = { Text("Имя") },
                singleLine = true,
                enabled = true,
                placeholder = { Text("Введите имя") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
            VerticalSpace(16.dp)
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = username,
                onValueChange = {
                    if (it.matches(usernamePattern)) username = it
                    error = null
                },
                label = { Text("Username") },
                singleLine = true,
                enabled = true,
                supportingText = { error?.let { Text(text = it, color = Color.Red) } },
                placeholder = { Text("Введите username") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
            VerticalSpace(16.dp)
            Row() {
                OutlinedButton(
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp),
                    onClick = { onBackClick?.invoke(phone ?: "") }
                ) {
                    Text(text = "Отмена")
                }
                OutlinedButton(
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp),
                    onClick = { onRegistrationClick?.invoke(phone ?: "", name, username) }
                ) {
                    Text(text = "Регистрация")
                }
            }
        }
    }
}
