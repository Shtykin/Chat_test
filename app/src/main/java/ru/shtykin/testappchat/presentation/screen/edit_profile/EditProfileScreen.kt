package ru.shtykin.testappchat.presentation.screen.edit_profile

import android.graphics.Bitmap
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import ru.shtykin.testappchat.R
import ru.shtykin.testappchat.domain.entity.Profile
import ru.shtykin.testappchat.presentation.screen.common_parts.HorizontalSpace
import ru.shtykin.testappchat.presentation.screen.common_parts.VerticalSpace
import ru.shtykin.testappchat.presentation.screen.edit_profile.DateDefaults.DATE_LENGTH
import ru.shtykin.testappchat.presentation.screen.edit_profile.DateDefaults.DATE_MASK
import ru.shtykin.testappchat.presentation.state.ScreenState
import ru.shtykin.testappchat.presentation.utils.DateTransformation


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    uiState: ScreenState,
    onChangeAvatarClick: (() -> Unit)?,
    onSaveClick: ((Profile) -> Unit)?,
    onCancelClick: (() -> Unit)?,
) {
    val profile = (uiState as? ScreenState.EditProfileScreen)?.profile
    val error = (uiState as? ScreenState.EditProfileScreen)?.error
    val isLoading = (uiState as? ScreenState.EditProfileScreen)?.isLoading ?: false
    var name: String by remember { mutableStateOf("") }
    var birthday: String by remember { mutableStateOf("") }
    var city: String by remember { mutableStateOf("") }
    var about: String by remember { mutableStateOf("") }
    var avatar: Bitmap? by remember { mutableStateOf(null) }
    var avatarUrl: String? by remember { mutableStateOf(null) }

    LaunchedEffect(key1 = Unit) {
        profile?.name?.let { name = it }
        profile?.birthday?.let { birthday = it }
        profile?.city?.let { city = it }
        profile?.status?.let { about = it }
        profile?.avatarUrl?.let { avatarUrl = it }
    }
    SideEffect {
        profile?.avatar?.let { avatar = it }
    }
    BackHandler { onCancelClick?.invoke() }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        SwipeRefresh(state = rememberSwipeRefreshState(isLoading), onRefresh = {  }) {
            Column(
                modifier = Modifier.padding(32.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                profile?.let { it ->
                    if (it.avatar != null) {
                        AsyncImage(
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(150.dp),
                            model = it.avatar,
                            contentScale = ContentScale.Crop,
                            contentDescription = null
                        )
                    } else if (avatarUrl != null) {
                        Image(
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(150.dp),
                            painter = rememberAsyncImagePainter(avatarUrl),
                            contentScale = ContentScale.Crop,
                            contentDescription = null
                        )
                    } else {
                        Image(
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(150.dp),
                            painter = painterResource(id = R.drawable.baseline_person_24),
                            contentDescription = null
                        )
                    }

                    VerticalSpace(height = 8.dp)
                    OutlinedButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { onChangeAvatarClick?.invoke() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddAPhoto,
                            contentDescription = null
                        )
                        HorizontalSpace(width = 8.dp)
                        Text(text = if (it.avatar != null && !avatarUrl.isNullOrEmpty()) "Сменить фото" else "Установить фото")
                    }
                    VerticalSpace(height = 8.dp)
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Имя") },
                        singleLine = true,
                    )
                    VerticalSpace(height = 8.dp)
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = birthday,
                        onValueChange = { if (it.length <= DATE_LENGTH) birthday = it },
                        placeholder = { Text(text = DATE_MASK, color = Color.Gray)},
                        visualTransformation = DateTransformation(DATE_MASK),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        label = { Text("Дата рождения") },
                        singleLine = true,
                    )
                    VerticalSpace(height = 8.dp)
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = city,
                        onValueChange = { city = it },
                        label = { Text("Город") },
                        singleLine = true,
                    )
                    VerticalSpace(height = 8.dp)
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        value = about,
                        onValueChange = { about = it },
                        label = { Text("О себе") },
                        maxLines = 3,
                    )
                    VerticalSpace(height = 8.dp)
                    Row() {
                        OutlinedButton(
                            modifier = Modifier
                                .weight(1f)
                                .padding(8.dp),
                            enabled = !isLoading,
                            onClick = { onCancelClick?.invoke() }
                        ) {
                            Text(text = "Отмена")
                        }
                        OutlinedButton(
                            modifier = Modifier
                                .weight(1f)
                                .padding(8.dp),
                            enabled = !isLoading,
                            onClick = {
                                onSaveClick?.invoke(
                                    Profile(
                                        phone = profile.phone,
                                        name = name,
                                        username = profile.username,
                                        birthday = birthday,
                                        zodiacSign = "",
                                        age = null,
                                        city = city,
                                        status = about,
                                        avatar = avatar,
                                        avatarUrl = profile.avatarUrl
                                    )
                                )
                            }
                        ) {
                            Text(text = "Сохранить")
                        }
                    }
                    error?.let{
                        VerticalSpace(4.dp)
                        Text(text = it, color = Color.Red, fontSize = 12.sp)
                    }
                }
            }
        }

    }
}


object DateDefaults {
    const val DATE_MASK = "##.##.####"
    const val DATE_LENGTH = 8
}