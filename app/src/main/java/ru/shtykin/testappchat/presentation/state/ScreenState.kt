package ru.shtykin.testappchat.presentation.state

import android.graphics.Bitmap
import ru.shtykin.testappchat.domain.entity.Country
import ru.shtykin.testappchat.domain.entity.Guest
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
        val profile: Profile?,
    ) : ScreenState()

    data class ChatScreen(
        val profile: Profile?,
        val guest: Guest
    ) : ScreenState()

    data class ProfileScreen(
        val profile: Profile?,
        val isLoading: Boolean,
        val error: String?
    ) : ScreenState()

    data class EditProfileScreen(
        val profile: Profile,
        val isLoading: Boolean,
        val error: String?
    ) : ScreenState()

}
