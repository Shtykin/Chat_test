package ru.shtykin.testappchat.presentation.state

import ru.shtykin.testappchat.domain.entity.Country


sealed class ScreenState {

    data class RegistrationScreen(
        val temp: String
    ) : ScreenState()

    data class LoginScreen(
        val country: Country?
    ) : ScreenState()

    data class ChooseCountryScreen(
        val countries: List<Country>
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
