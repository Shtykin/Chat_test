package ru.shtykin.testappchat.presentation.screen.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
//    onGetPictureClick: (() -> Unit)?,
//    onGetBase64Click: (() -> Unit)?,
) {
    val profile = (uiState as? ScreenState.ProfileScreen)?.profile


    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.Top
        ) {
            IconButton(onClick = { onEditProfileClick?.invoke() }) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = null
                )
            }
        }
        Column(
            modifier = Modifier.padding(32.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            profile?.let {

                Row(
                    modifier = Modifier.fillMaxWidth(),
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
                        Text(
                            text = it.username,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        VerticalSpace(height = 8.dp)
                        Text(
                            text = "${it.name}, ${it.city}",
                            fontSize = 16.sp
                        )
                        VerticalSpace(height = 4.dp)
                        Text(
                            text = "+${it.phone}",
                            fontSize = 16.sp
                        )

                    }
                }
                VerticalSpace(height = 16.dp)
                Row() {
                    Text(text = "Возраст: ")
                    if (it.birthday.isNotEmpty()) {
                        Text(text = it.age.toString())
                    } else {
                        Text(text = "не заполнено")
                    }
                }
                VerticalSpace(height = 8.dp)
                Row() {
                    Text(text = "Знак зодиака: ")
                    if (it.zodiacSign.isNotEmpty()) {
                        Text(text = it.zodiacSign)
                    } else {
                        Text(text = "не заполнено")
                    }
                }
                VerticalSpace(height = 8.dp)
                Row() {
                    Text(text = "О себе: ")
                    if (it.status.isNotEmpty()) {
                        Text(text = it.status)
                    } else {
                        Text(text = "не заполнено")
                    }
                }
            }


//            Button(onClick = { onGetPictureClick?.invoke() }) {
//                Text(text = "Выбрать изображение2")
//            }
//            VerticalSpace(height = 16.dp)
//            profile?.avatar?.let {
//                AsyncImage(
//                    modifier = Modifier.size(250.dp),
//                    model = it,
//                    contentDescription = null
//                )
//            }


        }
    }

}