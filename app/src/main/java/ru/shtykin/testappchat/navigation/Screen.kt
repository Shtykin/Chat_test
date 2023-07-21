package ru.shtykin.testappchat.navigation

sealed class Screen(
    val route: String
) {
    object Login: Screen(ROUTE_LOGIN)
    object Registration: Screen(ROUTE_REGISTRATION)
    object Chat: Screen(ROUTE_CHAT)
    object AllChats: Screen(ROUTE_ALL_CHATS)
    object Profile: Screen(ROUTE_PROFILE)
    object EditProfile: Screen(ROUTE_EDIT_PROFILE)

    private companion object {
        const val ROUTE_LOGIN = "login"
        const val ROUTE_REGISTRATION = "registration"
        const val ROUTE_CHAT = "chat"
        const val ROUTE_ALL_CHATS = "all chats"
        const val ROUTE_PROFILE = "profile"
        const val ROUTE_EDIT_PROFILE = "edit profile"
    }
}
