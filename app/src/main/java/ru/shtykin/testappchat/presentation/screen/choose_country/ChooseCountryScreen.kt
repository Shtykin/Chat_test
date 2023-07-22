package ru.shtykin.testappchat.presentation.screen.choose_country

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.shtykin.testappchat.domain.entity.Country
import ru.shtykin.testappchat.presentation.screen.common_parts.HorizontalSpace
import ru.shtykin.testappchat.presentation.state.ScreenState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun ChooseCountryScreen(
    uiState: ScreenState,
    onCountryClick: ((Country) -> Unit)?,
    onBackClick: (() -> Unit)?
) {
    var searchState by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    val regions = (uiState as? ScreenState.ChooseCountryScreen)?.countries
    var searchText by remember { mutableStateOf(TextFieldValue(text = "")) }
    val searchChanged: (TextFieldValue) -> Unit = {
        if (searchState) searchText = it.copy(text = it.text)
    }

    val searchFocusRequester = FocusRequester()
    if (searchState) {
        LaunchedEffect(key1 = null) {
            searchFocusRequester.requestFocus()
        }
    }

    BackHandler {
        if (searchState) {
            searchText = TextFieldValue("")
            searchState = false
        } else {
            onBackClick?.invoke()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (searchState) {
                        TextField(
                            value = searchText,
                            onValueChange = searchChanged,
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(searchFocusRequester),
                            placeholder = {
                                Text("Enter country or code")
                            },
                            textStyle = TextStyle(fontSize = 16.sp),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                            keyboardActions = KeyboardActions(onSearch = { keyboardController?.hide() }),
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = MaterialTheme.colorScheme.background,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent
                            )
                        )
                    } else {
                        Text(
                            modifier = Modifier.padding(start = 13.dp),
                            text = "Выберите страну",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                },
                navigationIcon = {
                    IconButton(onClick = {
                        if (searchState) {
                            searchText = TextFieldValue("")
                            searchState = false
                        } else {
                            onBackClick?.invoke()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Localized description"
                        )
                    }
                },
                actions = {
                    if (searchState) {
                        if (searchText.text.isNotEmpty()) {
                            IconButton(onClick = { searchText = TextFieldValue("") }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Localized description"
                                )
                            }
                        }
                    } else {
                        IconButton(onClick = { searchState = true }) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Localized description"
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxHeight(),
            contentPadding = innerPadding
        ) {
            item {
                Divider(
                    thickness = 1.dp,
                    color = Color.Gray
                )
            }
            regions?.filter {
                it.country.contains(
                    searchText.text,
                    ignoreCase = true
                ) || "+${it.code}".contains(searchText.text)
            }?.let {
                items(it) { region ->
                    regionCard(
                        region = region,
                        onCountryClick = onCountryClick
                    )
                }
            }

        }

    }

}

@Composable
fun regionCard(
    region: Country,
    onCountryClick: ((Country) -> Unit)?,
) {
    Box(
        modifier = Modifier
            .padding(16.dp)
            .clickable { onCountryClick?.invoke(region) },
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            HorizontalSpace(width = 16.dp)
            Text(text = region.flagEmoji ?: "")
            HorizontalSpace(width = 16.dp)
            Text(
                modifier = Modifier.weight(1f),
                text = region.country
            )
            Text(
                text = "+${region.code}",
                color = Color.Gray
            )
            HorizontalSpace(width = 16.dp)
        }
    }
}