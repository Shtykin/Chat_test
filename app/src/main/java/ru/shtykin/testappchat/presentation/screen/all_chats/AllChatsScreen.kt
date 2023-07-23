package ru.shtykin.testappchat.presentation.screen.all_chats

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import kotlinx.coroutines.launch
import ru.shtykin.testappchat.presentation.state.ScreenState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllChatsScreen(
    uiState: ScreenState,
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {

            }
        },
        content = {

        }
    )

    Scaffold() { paddingValues ->
        LazyColumn(contentPadding = paddingValues) {
            item { Text(text = "Список чатов") }
        }

    }

}
