package ru.shtykin.testappchat.presentation

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.Phonenumber
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.shtykin.testappchat.domain.entity.Country
import ru.shtykin.testappchat.domain.entity.Profile
import ru.shtykin.testappchat.domain.usecase.CheckAuthCodeUseCase
import ru.shtykin.testappchat.domain.usecase.RegistrationUseCase
import ru.shtykin.testappchat.domain.usecase.SendAuthCodeUseCase
import ru.shtykin.testappchat.presentation.state.ScreenState
import ru.shtykin.testappchat.settings.AuthStore
import ru.shtykin.testappchat.settings.ProfileStore
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authStore: AuthStore,
    private val profileStore: ProfileStore,
    private val registrationUseCase: RegistrationUseCase,
    private val sendAuthCodeUseCase: SendAuthCodeUseCase,
    private val checkAuthCodeUseCase: CheckAuthCodeUseCase
) : ViewModel() {

    private val _uiState =
        mutableStateOf<ScreenState>(
            ScreenState.LoginScreen(
                phone = null,
                country = null,
                isVisibleCodeField = false,
                error = null,
                errorCode = null
            )
        )

    val uiState: State<ScreenState>
        get() = _uiState

    private val phoneNumberUtil = PhoneNumberUtil.getInstance()


    fun tryToRequestSms(phone: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                parsePhone(phone)
                sendAuthCode(phone)
                _uiState.value = ScreenState.LoginScreen(
                    phone = phone,
                    isVisibleCodeField = true,
                    error = null,
                    errorCode = null
                )
            } catch (e: Exception) {
                _uiState.value = ScreenState.LoginScreen(
                    phone = phone,
                    isVisibleCodeField = false,
                    error = e.message,
                    errorCode = null
                )
            }
        }
    }

    fun tryToLogin(
        phone: String,
        code: String,
        onSuccess: (() -> Unit)? = null,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val userTokens = checkAuthCode(phone, code)
                authStore.apply {
                    this.phone = phone
                    this.accessToken = userTokens.accessToken
                    this.refreshToken = userTokens.refreshToken
                }
                withContext(Dispatchers.Main) {
                    onSuccess?.invoke()
                }
                _uiState.value = ScreenState.AllChatsChats("All Chats")
            } catch (e: Exception) {
                _uiState.value = ScreenState.LoginScreen(
                    phone = phone,
                    isVisibleCodeField = true,
                    error = null,
                    errorCode = e.message
                )
            }
        }

    }

    fun tryToRegister(
        phone: String,
        name: String,
        username: String,
        onSuccess: ((String) -> Unit)? = null,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val userTokens = registration(phone, name, username)
                authStore.apply {
                    this.phone = phone
                    this.accessToken = userTokens.accessToken
                    this.refreshToken = userTokens.refreshToken
                }
                Log.e("DEBUG", "access -> ${authStore.accessToken}")
                withContext(Dispatchers.Main) {
                    onSuccess?.invoke("Пользователь $username зарегистрирован")
                }
                _uiState.value = ScreenState.LoginScreen(
                    phone = phone,
                    isVisibleCodeField = false,
                    error = null,
                    errorCode = null
                )
            } catch (e: Exception) {
                Log.e("DEBUG", "tryToRegister e -> ${e.message}")
                _uiState.value = ScreenState.RegistrationScreen(
                    phone = phone,
                    name = name,
                    username = username,
                    error = e.message
                )
            }
        }
    }

    fun loginScreenOpened(country: Country) {
        _uiState.value = ScreenState.LoginScreen(
            country = country,
            isVisibleCodeField = false,
            error = null,
            errorCode = null
        )
    }

    fun loginScreenOpened(phone: String) {
        _uiState.value = ScreenState.LoginScreen(
            phone = phone,
            isVisibleCodeField = false,
            error = null,
            errorCode = null
        )
    }

    fun registrationScreenOpened(
        phone: String,
        onParseSuccess: (() -> Unit)? = null,
    ) {
        try {
            parsePhone(phone)
            _uiState.value = ScreenState.RegistrationScreen(
                phone = phone,
                name = "",
                username = "",
                error = null
            )
            onParseSuccess?.invoke()
        } catch (e: Exception) {
            _uiState.value = ScreenState.LoginScreen(
                phone = phone,
                isVisibleCodeField = false,
                error = e.message,
                errorCode = null
            )
        }
    }

    fun chooseCountryScreenOpened() {
        _uiState.value = ScreenState.ChooseCountryScreen(
            countries = getCountry("")
        )
    }

    fun profileScreenOpened() {
        _uiState.value = ScreenState.ProfileScreen(
            profile = Profile(
                name = "Евгений",
                username = "SuperMan",
                birthday = "20-04-1990",
                city = "Irkutst",
                avatar = getAvatarBitmap()
            )
        )
    }

    fun updateBmp() {
        _uiState.value = ScreenState.ProfileScreen(
            profile = Profile(
                name = "Евгений",
                username = "SuperMan",
                birthday = "20-04-1990",
                city = "Irkutst",
                avatar = getAvatarBitmap()
            ),
        )
    }

    private fun parsePhone(phone: String): Boolean {
        phoneNumberUtil.parse(
            phone,
            Phonenumber.PhoneNumber.CountryCodeSource.FROM_NUMBER_WITH_PLUS_SIGN.name
        )
        return true
    }

    private fun getCountry(searchText: String): List<Country> {
        val regionCodes = mutableListOf<Country>()
        val supportedRegions = phoneNumberUtil.supportedRegions
        supportedRegions.forEach {
            val countryCode = phoneNumberUtil.getCountryCodeForRegion(it)
            regionCodes.add(
                Country(
                    region = it,
                    country = Locale("en", it).displayCountry,
                    code = countryCode.toString(),
                    flagEmoji = getFlagEmojiUsingRegion(it)
                )
            )
        }
        return regionCodes
            .filter { it.country.contains(searchText, ignoreCase = true) }
            .sortedBy { it.country }
    }

    fun getFlagEmoji(phone: String): String {
        try {
            val region = try {
                val parsedPhone = phoneNumberUtil.parse(
                    phone,
                    Phonenumber.PhoneNumber.CountryCodeSource.FROM_NUMBER_WITH_PLUS_SIGN.name
                )
                phoneNumberUtil.getRegionCodeForCountryCode(parsedPhone.countryCode)
            } catch (e: Exception) {
                Locale.getDefault().country
            }
            Log.e("DEBUG", "region -> $region")
            if (region.length != 2 || region == "ZZ") {
                return DEFAULT_FLAG_EMOJI
            }
            val countryCodeCaps = region.uppercase(Locale.getDefault())
            val firstLetter = Character.codePointAt(countryCodeCaps, 0) - 0x41 + 0x1F1E6
            val secondLetter = Character.codePointAt(countryCodeCaps, 1) - 0x41 + 0x1F1E6
            if (!countryCodeCaps[0].isLetter() || !countryCodeCaps[1].isLetter()) {
                return DEFAULT_FLAG_EMOJI
            }
            return String(Character.toChars(firstLetter)) + String(Character.toChars(secondLetter))
        } catch (e: Exception) {
            return DEFAULT_FLAG_EMOJI
        }
    }

    private fun getFlagEmojiUsingRegion(region: String): String? {
        try {
            val countryCodeCaps = region.uppercase(Locale.getDefault())
            val firstLetter = Character.codePointAt(countryCodeCaps, 0) - 0x41 + 0x1F1E6
            val secondLetter = Character.codePointAt(countryCodeCaps, 1) - 0x41 + 0x1F1E6

            if (!countryCodeCaps[0].isLetter() || !countryCodeCaps[1].isLetter()) {
                return null
            }
            return String(Character.toChars(firstLetter)) + String(Character.toChars(secondLetter))
        } catch (e: Exception) {
            return null
        }
    }

    private fun getAvatarBitmap(): Bitmap {
        val decodedBytes = Base64.decode(profileStore.avatar, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }

    private suspend fun registration(phone: String, name: String, userName: String) =
        registrationUseCase.execute(phone, name, userName)

    private suspend fun sendAuthCode(phone: String) =
        sendAuthCodeUseCase.execute(phone)

    private suspend fun checkAuthCode(phone: String, code: String) =
        checkAuthCodeUseCase.execute(phone, code)

    companion object {
        private const val DEFAULT_FLAG_EMOJI = "\uD83C\uDDF7\uD83C\uDDFA"
    }
}