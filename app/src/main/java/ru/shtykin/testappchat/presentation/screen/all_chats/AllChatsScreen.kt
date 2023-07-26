package ru.shtykin.testappchat.presentation.screen.all_chats

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import ru.shtykin.testappchat.R
import ru.shtykin.testappchat.domain.entity.Guest
import ru.shtykin.testappchat.presentation.screen.common_parts.HorizontalSpace
import ru.shtykin.testappchat.presentation.screen.common_parts.VerticalSpace
import ru.shtykin.testappchat.presentation.state.ScreenState
import ru.shtykin.testappchat.presentation.utils.NavigationItem


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllChatsScreen(
    uiState: ScreenState,
    onProfileClick: (() -> Unit)?,
    onContactsClick: (() -> Unit)?,
    onSettingsClick: (() -> Unit)?,
    onLogoutClick: (() -> Unit)?,
    onSearchClick: (() -> Unit)?,
    onChatClick: ((Guest) -> Unit)?,
) {
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    val firstItemGroup = listOf(
        NavigationItem.Profile,
        NavigationItem.Contacts,
        NavigationItem.Settings
    )
    val secondItemGroup = listOf(
        NavigationItem.Logout
    )

    val profile = (uiState as? ScreenState.AllChatsChats)?.profile

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column() {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                            .background(MaterialTheme.colorScheme.primary)
                    ) {
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
                            onClick = {
                                if (item is NavigationItem.Profile) onProfileClick?.invoke()
                                if (item is NavigationItem.Contacts) onContactsClick?.invoke()
                                if (item is NavigationItem.Settings) onSettingsClick?.invoke()
                            },
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
                            onClick = {
                                if (item is NavigationItem.Logout) onLogoutClick?.invoke()
                            },
                            shape = RoundedCornerShape(0.dp)
                        )
                    }
                }
            }
        },
        content = {
            val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
            Scaffold(
                modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = stringResource(R.string.app_name),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(
                                    imageVector = Icons.Outlined.Menu,
                                    contentDescription = null
                                )
                            }
                        },
                        actions = {
                            IconButton(onClick = { onSearchClick?.invoke() }) {
                                Icon(
                                    imageVector = Icons.Outlined.Search,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.background
                                )
                            }
                        },
                        colors = TopAppBarDefaults.smallTopAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            titleContentColor = MaterialTheme.colorScheme.onPrimary,
                            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        scrollBehavior = scrollBehavior
                    )
                },
            ) { paddingValues ->
                LazyColumn(contentPadding = paddingValues) {
                    items(guests) {
                        ChatCard(
                            guest = it,
                            onClick = onChatClick
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun ChatCard(
    guest: Guest,
    onClick: ((Guest) -> Unit)?,
) {
    Card(
        modifier = Modifier.clickable { onClick?.invoke(guest) },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
    ) {
        VerticalSpace(height = 4.dp)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
        ) {
            Image(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(50.dp),
                painter = painterResource(id = guest.resId),
                contentScale = ContentScale.Crop,
                contentDescription = null
            )
            HorizontalSpace(width = 8.dp)
            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                Column(
                modifier = Modifier.weight(1f).padding(start =  4.dp),
                    verticalArrangement = Arrangement.Top
                ) {
                    Text(
                        text = guest.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.W500
                    )
                    VerticalSpace(height = 4.dp)
                    Text(
                        text = guest.lastMessage,
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Text(modifier = Modifier.padding(top = 4.dp, end = 10.dp), text = "${(10..23).random()}:${(10..59).random()}", color = Color.Gray, fontSize = 12.sp)
            }
            HorizontalSpace(width = 8.dp)
        }
        VerticalSpace(height = 4.dp)
        Divider(modifier = Modifier.padding(start = 70.dp, end = 16.dp),thickness = 1.dp, color = MaterialTheme.colorScheme.primary)
    }
}

val guests = listOf(
    Guest("Иван", R.drawable.photo1, "Как дела?"),
    Guest("Егор", R.drawable.photo2, "Пока!"),
    Guest("Ксю", R.drawable.photo3, "Сегодня до четырех часов)"),
    Guest("Кейт", R.drawable.photo4, "не знаю даже"),
    Guest("Макс", R.drawable.photo5, "ку!"),
    Guest("Леший", R.drawable.photo6, "У Лукоморья дуб... есть!"),
    Guest("Княжна", R.drawable.photo7, "Стремитесь не к успеху, а к ценностям, которые он дает"),
    Guest("Марья", R.drawable.photo8, "Духовной жаждою томим В пустыне мрачной я влачился"),
    Guest("Полезный", R.drawable.photo9, "Наука — это организованные знания, мудрость — это организованная жизнь"),
    Guest("Шеф", R.drawable.photo10, "Айти?"),
    Guest("Евген", R.drawable.photo11, "Свобода ничего не стоит, если она не включает в себя свободу ошибаться"),
    Guest("Оля", R.drawable.photo12, "Лучшая месть – огромный успех"),
    Guest("Настя", R.drawable.photo13, "Три девицы под окном Пряли поздно вечерком"),
    Guest("Миша", R.drawable.photo14, "Потонула деревня в ухабинах"),
    Guest("Батя", R.drawable.photo15, "Печально я гляжу на наше поколенье!"),
)