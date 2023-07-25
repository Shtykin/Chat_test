package ru.shtykin.testappchat.presentation.screen.profile

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import ru.shtykin.testappchat.R
import ru.shtykin.testappchat.presentation.screen.common_parts.HorizontalSpace
import ru.shtykin.testappchat.presentation.screen.common_parts.VerticalSpace
import ru.shtykin.testappchat.presentation.state.ScreenState


@Composable
fun ProfileScreen(
    uiState: ScreenState,
    onEditProfileClick: (() -> Unit)?,
    onBackClick: (() -> Unit)?,
) {
    val profile = (uiState as? ScreenState.ProfileScreen)?.profile
    val error = (uiState as? ScreenState.ProfileScreen)?.error
    val isLoading = (uiState as? ScreenState.ProfileScreen)?.isLoading ?: false

    BackHandler { onBackClick?.invoke() }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            } else {
                profile?.let {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            if (it.avatarUrl != null) {
                                Image(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .size(100.dp),
                                    painter = rememberAsyncImagePainter(it.avatarUrl),
                                    contentScale = ContentScale.Crop,
                                    contentDescription = null
                                )
                            } else {
                                Image(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .size(100.dp),
                                    painter = painterResource(id = R.drawable.baseline_person_24),
                                    contentDescription = null
                                )
                            }

                            HorizontalSpace(width = 16.dp)
                            Column() {
                                Row() {
                                    Text(
                                        text = it.username ?: "",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Spacer(modifier = Modifier.weight(1f))
                                    IconButton(
                                        modifier = Modifier.offset(x = 16.dp, y = (-16).dp),
                                        onClick = { onEditProfileClick?.invoke() }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.Settings,
                                            contentDescription = null
                                        )
                                    }
                                }
                                Text(
                                    text = "${it.name}, ${it.city}",
                                    fontSize = 16.sp
                                )
                                VerticalSpace(height = 8.dp)
                                Text(
                                    text = "+${it.phone}",
                                    fontSize = 16.sp
                                )
                            }
                            Spacer(modifier = Modifier.weight(1f))

                        }
                    }
                    VerticalSpace(height = 16.dp)
                    Row() {
                        Text(text = "Возраст: ")
                        if (!it.age.isNullOrEmpty()) {
                            Text(text = it.age.toString())
                        } else {
                            Text(text = "не заполнено")
                        }
                    }
                    VerticalSpace(height = 8.dp)
                    Row() {
                        Text(text = "Знак зодиака: ")
                        if (!it.zodiacSign.isNullOrEmpty()) {
                            Text(text = it.zodiacSign)
                        } else {
                            Text(text = "не заполнено")
                        }
                    }
                    VerticalSpace(height = 8.dp)
                    Row() {
                        Text(text = "О себе: ")
                        if (!it.status.isNullOrEmpty()) {
                            Text(text = it.status)
                        } else {
                            Text(text = "не заполнено")
                        }
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