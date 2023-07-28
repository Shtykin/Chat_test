package ru.shtykin.testappchat.presentation.screen.chat

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material.icons.outlined.TagFaces
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import ru.shtykin.testappchat.R
import ru.shtykin.testappchat.domain.entity.Guest
import ru.shtykin.testappchat.domain.entity.Message
import ru.shtykin.testappchat.domain.entity.Sender
import ru.shtykin.testappchat.presentation.screen.common_parts.HorizontalSpace
import ru.shtykin.testappchat.presentation.screen.common_parts.ModalDrawSheet
import ru.shtykin.testappchat.presentation.screen.common_parts.VerticalSpace
import ru.shtykin.testappchat.presentation.state.ScreenState
import ru.shtykin.testappchat.presentation.utils.NavigationItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@SuppressLint("MutableCollectionMutableState")
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
    var messages by remember { mutableStateOf(if (guest != null) getFakeMessages(guest) else listOf()) }

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
                                    painter = painterResource(
                                        id = guest?.resId ?: R.drawable.baseline_person_24
                                    ),
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
                                        text = "был(а) только что",
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
                        onValueChange = { message = it },
                        maxLines = 3,
                        shape = RoundedCornerShape(0.dp),
                        leadingIcon = {
                            IconButton(onClick = { /*TODO*/ }) {
                                Icon(imageVector = Icons.Outlined.TagFaces, contentDescription = null)
                            }
                        },
                        trailingIcon = {
                            IconButton(onClick = {
                                if (message.isNotEmpty()) {
                                    messages = messages + Message(
                                        text = message,
                                        sender = Sender.Me,
                                        timeStamp = System.currentTimeMillis()
                                    )
                                    message = ""
                                }
                            }) {
                                Icon(imageVector = Icons.Outlined.Send, contentDescription = null)
                            }
                        }
                    )
                }
            ) { paddingValues ->
                LazyColumn(
                    modifier = Modifier.padding(paddingValues),
                    reverseLayout = true
                ) {
                    items(messages.reversed()) {
                        Message(it)
                    }
                }
            }
        }
    )
}

@Composable
fun Message(
    message: Message
) {
    val arrangement = if (message.sender is Sender.Me) Arrangement.End else Arrangement.Start
    val color = if (message.sender is Sender.Me)
        CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    else
        CardDefaults.cardColors(containerColor = Color.White)
    val shape = if (message.sender is Sender.Me)
        RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp, bottomStart = 8.dp, bottomEnd = 0.dp)
    else
        RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp, bottomStart = 0.dp, bottomEnd = 8.dp)
    val modifier = if (message.sender is Sender.Me)
        Modifier.padding(start = 24.dp, end = 8.dp, top = 4.dp, bottom = 4.dp)
    else
        Modifier.padding(start = 8.dp, end = 24.dp, top = 4.dp, bottom = 4.dp)

    val timeFormatted = try {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        val date = Date(message.timeStamp)
         sdf.format(date)
    } catch (e: Exception) {
        "00:01"
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = arrangement
    ) {
        Card(
            modifier = modifier,
            shape = shape,
            colors = color,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
        ) {
            Row(
                verticalAlignment = Alignment.Bottom
            ) {
                Column() {
                    Text(
                        modifier = Modifier.padding(top = 4.dp, start = 8.dp, end = 8.dp),
                        text = message.text,
                        fontSize = 17.sp
                    )
                    VerticalSpace(height = 16.dp)
                }
                Text(
                    modifier = Modifier.padding(end = 4.dp),
                    text = timeFormatted,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }

}

fun getFakeMessages(guest: Guest) = listOf(
    Message("Привет!. Как ты?", Sender.Me, System.currentTimeMillis() - 1360000),
    Message("Огонь!", Sender.NotMe(guest), System.currentTimeMillis() - 1260000),
    Message("Рад за тебя)", Sender.Me, System.currentTimeMillis() - 1160000),
    Message("Расскажи стишок что ли...)", Sender.Me, System.currentTimeMillis() - 560000),
    Message(
        "Духовной жаждою томим,\n" +
                "В пустыне мрачной я влачился, —\n" +
                "И шестикрылый серафим\n" +
                "На перепутье мне явился.\n" +
                "Перстами легкими как сон\n" +
                "Моих зениц коснулся он.\n" +
                "Отверзлись вещие зеницы,\n" +
                "Как у испуганной орлицы.", Sender.NotMe(guest), System.currentTimeMillis() - 460000
    ),
    Message("Круто! а дальше?", Sender.Me, System.currentTimeMillis() - 360000),
    Message(
        "Моих ушей коснулся он, —\n" +
                "И их наполнил шум и звон:\n" +
                "И внял я неба содроганье,\n" +
                "И горний ангелов полет,\n" +
                "И гад морских подводный ход,\n" +
                "И дольней лозы прозябанье.\n" +
                "И он к устам моим приник,\n" +
                "И вырвал грешный мой язык,\n" +
                "И празднословный и лукавый,\n" +
                "И жало мудрыя змеи\n" +
                "В уста замершие мои\n" +
                "Вложил десницею кровавой.", Sender.NotMe(guest), System.currentTimeMillis() - 260000
    ),
    Message("Вещь!", Sender.Me, System.currentTimeMillis() - 180000),
    Message(
        "Это взорвет интернет )",
        Sender.Me,
        System.currentTimeMillis() - 60000
    ),
)
