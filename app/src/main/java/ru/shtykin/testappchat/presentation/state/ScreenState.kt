package ru.shtykin.testappchat.presentation.state



sealed class ScreenState {

    data class RegistrationScreen(
        val temp: String
    ) : ScreenState()

    data class LoginScreen(
        val temp: String
    ) : ScreenState()

    data class AllChatsChats(
        val temp: String
    ) : ScreenState()

    data class ChatScreen(
        val temp: String
    ) : ScreenState()

    data class ProfileScreen(
        val temp: String
    ) : ScreenState()

    data class EditProfileScreen(
        val temp: String
    ) : ScreenState()

}
