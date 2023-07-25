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
import ru.shtykin.testappchat.domain.usecase.GetProfileUseCase
import ru.shtykin.testappchat.domain.usecase.PutProfileUseCase
import ru.shtykin.testappchat.domain.usecase.RegistrationUseCase
import ru.shtykin.testappchat.domain.usecase.SendAuthCodeUseCase
import ru.shtykin.testappchat.presentation.state.ScreenState
import ru.shtykin.testappchat.settings.AuthStore
import ru.shtykin.testappchat.settings.ProfileStore
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authStore: AuthStore,
    private val profileStore: ProfileStore,
    private val registrationUseCase: RegistrationUseCase,
    private val sendAuthCodeUseCase: SendAuthCodeUseCase,
    private val checkAuthCodeUseCase: CheckAuthCodeUseCase,
    private val getProfileUseCase: GetProfileUseCase,
    private val putProfileUseCase: PutProfileUseCase
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

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val profile = getProfile()
                profileStore.apply {
                    name = profile.name
                    username = profile.username
                    birthday = profile.birthday
                    city = profile.city
                    avatar = getAvatarBase64(profile.avatar)
                    about = profile.status
                    avatarUrl = profile.avatarUrl ?: ""
                }
                _uiState.value = ScreenState.ProfileScreen(
                    profile = profile
                )
            } catch (e: Exception) {
                Log.e("DEBUG1", "Exception -> ${e.message}")
            }
        }

    }

    fun editProfileScreenOpened() {
        _uiState.value = ScreenState.EditProfileScreen(
            profile = Profile(
                phone = authStore.phone,
                name = profileStore.name,
                username = profileStore.username,
                birthday = profileStore.birthday,
                zodiacSign = getZodiacSign(Date(1990, 5, 20)),
                age = getAge(profileStore.birthday),
                city = profileStore.city,
                status = profileStore.about,
                avatar = getAvatarBitmap(profileStore.avatar),
                avatarUrl = profileStore.avatarUrl.ifEmpty { null }
            )
        )
    }

    fun saveProfile(
        profile: Profile,
        onSuccess: (() -> Unit)? = null,
    ) {
        profileStore.apply {
            name = profile.name
            birthday = profile.birthday
            city = profile.city
            avatar = getAvatarBase64(profile.avatar)
            about = profile.status
        }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = putProfile(profile)
                Log.e("DEBUG1", "put result -> $result")
                if (result) {
                    val newProfile = getProfile()
                    profileStore.apply {
                        name = newProfile.name
                        username = newProfile.username
                        birthday = newProfile.birthday
                        city = newProfile.city
                        avatar = getAvatarBase64(newProfile.avatar)
                        about = newProfile.status
                        avatarUrl = newProfile.avatarUrl ?: ""
                    }
                    _uiState.value = ScreenState.ProfileScreen(
                        profile = newProfile
                    )
                    withContext(Dispatchers.Main) {
                        onSuccess?.invoke()
                    }
                }
            } catch (e: Exception) {
                Log.e("DEBUG1", "ex -> ${e.message}")
            }
        }

    }


    fun updateBmp(base64: String) {
        if (_uiState.value is ScreenState.EditProfileScreen) {
            val profile = (_uiState.value as ScreenState.EditProfileScreen).profile.copy(avatar = getAvatarBitmap(base64))
            _uiState.value = ScreenState.EditProfileScreen(profile)
        }
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

    private fun getAvatarBitmap(base64: String): Bitmap? {
        return try {
            val decodedBytes = Base64.decode(base64, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: Exception) {
            null
        }
    }

    private fun getAvatarBase64(bitmap: Bitmap?): String {
        if (bitmap == null) return ""
        return try {
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            val byteArray = outputStream.toByteArray()
            Base64.encodeToString(byteArray, Base64.DEFAULT)
        } catch (e: Exception) {
            ""
        }
    }

    private fun getAvatarBitmapFromStorage(base64: String): Bitmap? {
        return try {
            val decodedBytes = Base64.decode(base64, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: Exception) {
            null
        }
    }

    private fun getBirthdayString(birthday: Date): String {
        val sdf = SimpleDateFormat("d MMMM", Locale.getDefault())
        val date = Date(birthday.time)
        return sdf.format(date)
    }

    private fun getZodiacSign(birthday: Date): String {
        val date = Date(birthday.time)
        val day = date.day
        val zodiacSign = when (date.month) {
            1 -> if (day <= 20) "Козерог" else "Водолей"
            2 -> if (day <= 19) "Водолей" else "Рыбы"
            3 -> if (day <= 20) "Рыбы" else "Овен"
            4 -> if (day <= 20) "Овен" else "Телец"
            5 -> if (day <= 20) "Телец" else "Близнецы"
            6 -> if (day <= 21) "Близнецы" else "Рак"
            7 -> if (day <= 22) "Рак" else "Лев"
            8 -> if (day <= 23) "Лев" else "Дева"
            9 -> if (day <= 23) "Дева" else "Весы"
            10 -> if (day <= 23) "Весы" else "Скорпион"
            11 -> if (day <= 22) "Скорпион" else "Стрелец"
            12 -> if (day <= 21) "Стрелец" else "Козерог"
            else -> ""
        }
        return zodiacSign
    }

    private fun getAge(birthday:String): Int? {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val birthdayTimestamp = try {
            sdf.parse(birthday)?.time
        } catch (e: Exception) {
            return null
        } ?: return null
        val currentDate = System.currentTimeMillis()
        return Date(currentDate).year - Date(birthdayTimestamp).year
    }

    private suspend fun registration(phone: String, name: String, userName: String) =
        registrationUseCase.execute(phone, name, userName)

    private suspend fun sendAuthCode(phone: String) =
        sendAuthCodeUseCase.execute(phone)

    private suspend fun checkAuthCode(phone: String, code: String) =
        checkAuthCodeUseCase.execute(phone, code)

    private suspend fun getProfile() = getProfileUseCase.execute()

    private suspend fun putProfile(profile: Profile) = putProfileUseCase.execute(profile)

    companion object {
        private const val DEFAULT_FLAG_EMOJI = "\uD83C\uDDF7\uD83C\uDDFA"
    }
}