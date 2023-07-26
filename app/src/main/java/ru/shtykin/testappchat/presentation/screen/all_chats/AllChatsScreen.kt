package ru.shtykin.testappchat.presentation.screen.all_chats

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Search
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import ru.shtykin.testappchat.R
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
                                    fontSize = 16.sp
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
//                    VerticalSpace(height = 4.dp)
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
                        title = { Text(text = stringResource(R.string.app_name)) },
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

                }

            }
        }
    )


}
