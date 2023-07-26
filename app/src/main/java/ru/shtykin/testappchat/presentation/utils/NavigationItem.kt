package ru.shtykin.testappchat.presentation.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavigationItem(
    val title: String,
    val icon: ImageVector,
) {
    object Profile: NavigationItem(
        title = "Профиль",
        icon = Icons.Outlined.Person,
    )
    object Contacts: NavigationItem(
        title = "Контакты",
        icon = Icons.Outlined.Groups,
    )
    object Settings: NavigationItem(
        title = "Настройки",
        icon = Icons.Outlined.Settings,
    )
    object Logout: NavigationItem(
        title = "Log out",
        icon = Icons.Outlined.Logout,
    )
}