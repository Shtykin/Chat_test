package ru.shtykin.testappchat.presentation.screen.chat

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import ru.shtykin.testappchat.R
import ru.shtykin.testappchat.domain.entity.Guest
import ru.shtykin.testappchat.presentation.screen.all_chats.ChatCard
import ru.shtykin.testappchat.presentation.screen.all_chats.guests
import ru.shtykin.testappchat.presentation.screen.common_parts.HorizontalSpace
import ru.shtykin.testappchat.presentation.screen.common_parts.ModalDrawSheet
import ru.shtykin.testappchat.presentation.state.ScreenState
import ru.shtykin.testappchat.presentation.utils.NavigationItem


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    uiState: ScreenState,
    onProfileClick: (() -> Unit)?,
    onContactsClick: (() -> Unit)?,
    onSettingsClick: (() -> Unit)?,
    onLogoutClick: (() -> Unit)?,
    onSearchClick: (() -> Unit)?,
    onBackClick: (() -> Unit)?,
) {
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    val profile = (uiState as? ScreenState.ChatScreen)?.profile
    val guest = (uiState as? ScreenState.ChatScreen)?.guest
    var message by remember { mutableStateOf("") }

    BackHandler { onBackClick?.invoke() }
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawSheet(
                profile = profile,
                onItemClick = {
                    when (it) {
                        is NavigationItem.Profile -> onProfileClick?.invoke()
                        is NavigationItem.Contacts -> onContactsClick?.invoke()
                        is NavigationItem.Settings -> onSettingsClick?.invoke()
                        is NavigationItem.Logout -> onLogoutClick?.invoke()
                    }
                }
            )
        },
        content = {
            val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
            Scaffold(
                modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                topBar = {
                    TopAppBar(
                        title = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Image(
                                        modifier = Modifier
                                            .clip(CircleShape)
                                            .size(50.dp)
                                            .background(Color.White),
                                        painter = painterResource(id = guest?.resId ?: R.drawable.baseline_person_24),
                                        contentScale = ContentScale.Crop,
                                        contentDescription = null
                                    )
                                    HorizontalSpace(width = 8.dp)
                                    Column() {
                                        Text(
                                            text = guest?.name ?: "",
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        Text(
                                            text = "был(а) 3 часа назад",
                                            fontSize = 14.sp,
                                            color = Color.LightGray
                                        )
                                    }
                                }

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
                bottomBar = {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = message,
                        onValueChange = {message = it},
                        maxLines = 3,
                        shape = RoundedCornerShape(0.dp),
                        leadingIcon = { IconButton(onClick = { /*TODO*/ }) {
                            Icon(imageVector = Icons.Outlined.Face, contentDescription = null)
                        }},
                        trailingIcon = { IconButton(onClick = { /*TODO*/ }) {
                            Icon(imageVector = Icons.Outlined.Send, contentDescription = null)
                        }}
                    )
                }
            ) { paddingValues ->
                LazyColumn(contentPadding = paddingValues) {
                    items(5) {
                        Text(text = "Сообщение")
                    }
                }
            }
        }
    )
}

