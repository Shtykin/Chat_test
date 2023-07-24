package ru.shtykin.testappchat.presentation.state

import android.graphics.Bitmap
import ru.shtykin.testappchat.domain.entity.Country
import ru.shtykin.testappchat.domain.entity.Profile


sealed class ScreenState {

    data class RegistrationScreen(
        val phone: String,
        val name: String,
        val username: String,
        val error: String?
    ) : ScreenState()

    data class LoginScreen(
        val phone: String? = null,
        val country: Country? = null,
        val isVisibleCodeField: Boolean,
        val error: String?,
        val errorCode: String?
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
        val profile: Profile,
    ) : ScreenState()

    data class EditProfileScreen(
        val profile: Profile,
    ) : ScreenState()

}
