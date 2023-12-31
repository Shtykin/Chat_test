package ru.shtykin.testappchat.presentation.screen.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.shtykin.testappchat.R
import ru.shtykin.testappchat.presentation.screen.common_parts.HorizontalSpace
import ru.shtykin.testappchat.presentation.screen.common_parts.VerticalSpace
import ru.shtykin.testappchat.presentation.state.ScreenState
import ru.shtykin.testappchat.presentation.utils.PhoneNumberVisualTransformation


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(
    uiState: ScreenState,
    getFlagEmoji: ((String) -> String?),
    onEmojiClick: (() -> Unit)?,
    onRegistrationClick: ((String) -> Unit)?,
    onRequestSmsClick: ((String) -> Unit)?,
    onLoginClick: ((String, String) -> Unit)?,
    onCancelClick: ((String) -> Unit)?,
) {
    val country = (uiState as? ScreenState.LoginScreen)?.country
    val phoneNumber = (uiState as? ScreenState.LoginScreen)?.phone
    val isVisibleCodeField = (uiState as? ScreenState.LoginScreen)?.isVisibleCodeField ?: false
    val errorMessage = (uiState as? ScreenState.LoginScreen)?.error
    val errorCodeMessage = (uiState as? ScreenState.LoginScreen)?.errorCode

    var flagEmoji by remember {
        mutableStateOf(
            getFlagEmoji.invoke("") ?: ""
        )
    }

    var error: String? by remember { mutableStateOf(null) }
    var errorCode: String? by remember { mutableStateOf(null) }
    errorCodeMessage?.let { errorCode = it }

    var phone by remember {
        mutableStateOf(
            TextFieldValue(
                text = ""
            )
        )
    }
    SideEffect() {
        phoneNumber?.let {
            phone = phone.copy(
                text = it
            )
            flagEmoji = getFlagEmoji.invoke(it) ?: ""
        }
        error = errorMessage
    }

    val phoneChanged: (TextFieldValue) -> Unit = {
        error = null
        if (it.text.length <= PHONE_LENGTH) {
            phone = it.copy(
                text = "+" + it.text.filter { char -> char.isDigit() },
                selection = TextRange(it.text.length + 1)
            )
            getFlagEmoji.invoke(phone.text)?.let { emoji ->
                flagEmoji = emoji
            }
        }
    }

    country?.let {
        phone = TextFieldValue(
            text = "+${country.code}",
            selection = TextRange("+${country.code}".length)
        )
        it.flagEmoji?.let { emoji ->
            flagEmoji = emoji
        }
    }

    var code by remember { mutableStateOf("") }

    var char1 by remember { mutableStateOf("") }
    var char2 by remember { mutableStateOf("") }
    var char3 by remember { mutableStateOf("") }
    var char4 by remember { mutableStateOf("") }
    var char5 by remember { mutableStateOf("") }
    var char6 by remember { mutableStateOf("") }

    val char1FocusRequester = FocusRequester()
    val char2FocusRequester = FocusRequester()
    val char3FocusRequester = FocusRequester()
    val char4FocusRequester = FocusRequester()
    val char5FocusRequester = FocusRequester()
    val char6FocusRequester = FocusRequester()

    var visible by remember { mutableStateOf(false) }
    SideEffect {
        visible = true
    }
    val density = LocalDensity.current

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.padding(32.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            VerticalSpace(height = 25.dp)
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(
                    animationSpec = tween(2000, 500),
                    initialAlpha = 0f
                )
            ) {
                Image(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10))
                        .size(50.dp),
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                )
            }
            VerticalSpace(height = 25.dp)
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = phone,
                onValueChange = phoneChanged,
                label = { Text("Номер телефона") },
                singleLine = true,
                enabled = !isVisibleCodeField,
                supportingText = { error?.let { Text(text = it, color = Color.Red) } },
                placeholder = { Text("Введите номер телефона") },
                leadingIcon = {
                    Text(
                        modifier = Modifier.clickable { onEmojiClick?.invoke() },
                        text = flagEmoji
                    )
                },
                visualTransformation = PhoneNumberVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
            )
            VerticalSpace(16.dp)
            Row() {
                OutlinedButton(
                    enabled = !isVisibleCodeField,
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp),
                    onClick = { onRegistrationClick?.invoke(phone.text) }
                ) {
                    Text(text = "Регистрация")
                }
                OutlinedButton(
                    enabled = !isVisibleCodeField,
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp),
                    onClick = { onRequestSmsClick?.invoke(phone.text) }
                ) {
                    Text(text = "Вход по СМС")
                }
            }
            VerticalSpace(16.dp)
            if (isVisibleCodeField) {
                Text(text = "На номер ${phone.text} отправлена СМС с кодом. Введите его:")
                VerticalSpace(16.dp)
                Row(modifier = Modifier.fillMaxWidth()) {
                    CodeSymbolBox(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .focusRequester(char1FocusRequester),
                        value = char1,
                        valueChanged = {
                            char1 = it.take(1)
                            if (it.isNotEmpty()) char2FocusRequester.requestFocus()
                            errorCode = null
                        }
                    )
                    HorizontalSpace(width = 4.dp)
                    CodeSymbolBox(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .focusRequester(char2FocusRequester)
                            .onKeyEvent {
                                if (it.type == KeyEventType.KeyUp && it.key == Key.Backspace) {
                                    char1FocusRequester.requestFocus()
                                }
                                return@onKeyEvent true
                            },
                        value = char2,
                        valueChanged = {
                            char2 = it.take(1)
                            if (it.isNotEmpty()) char3FocusRequester.requestFocus()
                            errorCode = null
                        }
                    )
                    HorizontalSpace(width = 4.dp)
                    CodeSymbolBox(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .focusRequester(char3FocusRequester)
                            .onKeyEvent {
                                if (it.type == KeyEventType.KeyUp && it.key == Key.Backspace) {
                                    char2FocusRequester.requestFocus()
                                }
                                return@onKeyEvent true
                            },
                        value = char3,
                        valueChanged = {
                            char3 = it.take(1)
                            if (it.isNotEmpty()) char4FocusRequester.requestFocus()
                            errorCode = null
                        }
                    )
                    HorizontalSpace(width = 4.dp)
                    CodeSymbolBox(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .focusRequester(char4FocusRequester)
                            .onKeyEvent {
                                if (it.type == KeyEventType.KeyUp && it.key == Key.Backspace) {
                                    char3FocusRequester.requestFocus()
                                }
                                return@onKeyEvent true
                            },
                        value = char4,
                        valueChanged = {
                            char4 = it.take(1)
                            if (it.isNotEmpty()) char5FocusRequester.requestFocus()
                            errorCode = null
                        }
                    )
                    HorizontalSpace(width = 4.dp)
                    CodeSymbolBox(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .focusRequester(char5FocusRequester)
                            .onKeyEvent {
                                if (it.type == KeyEventType.KeyUp && it.key == Key.Backspace) {
                                    char4FocusRequester.requestFocus()
                                }
                                return@onKeyEvent true
                            },
                        value = char5,
                        valueChanged = {
                            char5 = it.take(1)
                            if (it.isNotEmpty()) char6FocusRequester.requestFocus()
                            errorCode = null
                        }
                    )
                    HorizontalSpace(width = 4.dp)
                    CodeSymbolBox(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .focusRequester(char6FocusRequester)
                            .onKeyEvent {
                                if (it.type == KeyEventType.KeyUp && it.key == Key.Backspace) {
                                    char5FocusRequester.requestFocus()
                                }
                                return@onKeyEvent true
                            },
                        value = char6,
                        valueChanged = {
                            char6 = it.take(1)
                            errorCode = null
                        },
                        keyboardActions = KeyboardActions(onDone = {
                            code = char1 + char2 + char3 + char4 + char5 + char6
                            onLoginClick?.invoke(phone.text, code)
                        }),
                    )
                }
                errorCode?.let {
                    VerticalSpace(4.dp)
                    Text(text = it, color = Color.Red, fontSize = 12.sp)
                }
                VerticalSpace(16.dp)
                Row() {
                    OutlinedButton(
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp),
                        onClick = {
                            onCancelClick?.invoke(phone.text)
                        }) {
                        Text(text = "Отмена")
                    }
                    OutlinedButton(
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp),
                        onClick = {
                            code = char1 + char2 + char3 + char4 + char5 + char6
                            onLoginClick?.invoke(phone.text, code)
                        }) {
                        Text(text = "Продолжить")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CodeSymbolBox(
    modifier: Modifier,
    value: String,
    valueChanged: (String) -> Unit,
    keyboardActions: KeyboardActions = KeyboardActions.Default
    ) {
    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = valueChanged,
        singleLine = true,
        enabled = true,
        visualTransformation = PhoneNumberVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        keyboardActions = keyboardActions,
        )
}

const val PHONE_LENGTH = 12