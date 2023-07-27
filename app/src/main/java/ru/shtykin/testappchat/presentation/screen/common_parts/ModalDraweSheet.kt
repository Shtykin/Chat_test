package ru.shtykin.testappchat.presentation.screen.common_parts

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import ru.shtykin.testappchat.R
import ru.shtykin.testappchat.domain.entity.Profile
import ru.shtykin.testappchat.presentation.utils.NavigationItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalDrawSheet(
    profile: Profile?,
    onItemClick: ((NavigationItem) -> Unit)?,
) {
    val firstItemGroup = listOf(
        NavigationItem.Profile,
        NavigationItem.Contacts,
        NavigationItem.Settings
    )
    val secondItemGroup = listOf(
        NavigationItem.Logout
    )

    ModalDrawerSheet {
        Column() {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                profile?.let { profile ->
                    Row(modifier = Modifier.padding(8.dp)) {
                        if (profile?.avatar == null) {
                            Image(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .size(50.dp)
                                    .background(Color.White),
                                painter = painterResource(id = R.drawable.baseline_person_24),
                                contentDescription = null
                            )
                        } else
                            AsyncImage(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .size(50.dp),
                                model = profile.avatar,
                                contentScale = ContentScale.Crop,
                                contentDescription = null
                            )

                        HorizontalSpace(width = 8.dp)
                        Column {
                            Text(
                                text = profile?.username ?: "",
                                color = Color.White,
                                fontSize = 18.sp
                            )
                            VerticalSpace(height = 4.dp)
                            Text(
                                text = profile?.phone ?: "",
                                color = Color.White,
                                fontSize = 14.sp
                            )
                        }
                    }
                }


            }
            firstItemGroup.forEach { item ->
                NavigationDrawerItem(
                    icon = { Icon(item.icon, contentDescription = null) },
                    label = {
                        Text(
                            text = item.title,
                            fontWeight = FontWeight.SemiBold
                        )
                    },
                    selected = false,
                    onClick = { onItemClick?.invoke(item) },
                    shape = RoundedCornerShape(0.dp)
                )
            }
            Divider(thickness = 1.dp)
            secondItemGroup.forEach { item ->
                NavigationDrawerItem(
                    icon = { Icon(item.icon, contentDescription = null) },
                    label = {
                        Text(
                            text = item.title,
                            fontWeight = FontWeight.SemiBold
                        )
                    },
                    selected = false,
                    onClick = { onItemClick?.invoke(item) },
                    shape = RoundedCornerShape(0.dp)
                )
            }
        }
    }
}

