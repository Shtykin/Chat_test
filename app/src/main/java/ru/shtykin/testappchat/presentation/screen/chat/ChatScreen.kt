package ru.shtykin.testappchat.presentation.screen.chat

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import ru.shtykin.testappchat.R
import ru.shtykin.testappchat.domain.entity.Guest
import ru.shtykin.testappchat.presentation.screen.all_chats.ChatCard
import ru.shtykin.testappchat.presentation.screen.all_chats.guests
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
) {
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    val profile = (uiState as? ScreenState.ChatScreen)?.profile

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
                    items(5) {
                        Text(text = "Сообщение")
                    }
                }
            }
        }
    )
}

