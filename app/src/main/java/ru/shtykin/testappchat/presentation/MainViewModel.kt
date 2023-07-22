package ru.shtykin.testappchat.presentation

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.Phonenumber
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.shtykin.testappchat.domain.entity.Country
import ru.shtykin.testappchat.domain.usecase.GetCurrentRegionUseCase
import ru.shtykin.testappchat.presentation.state.ScreenState
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getCurrentRegionUseCase: GetCurrentRegionUseCase
) : ViewModel() {

    private val _uiState =
        mutableStateOf<ScreenState>(
            ScreenState.ProfileScreen("")
        )

    val uiState: State<ScreenState>
        get() = _uiState

    private val phoneNumberUtil = PhoneNumberUtil.getInstance()



    fun loginScreenOpened(country: Country) {
        viewModelScope.launch {
            _uiState.value = ScreenState.LoginScreen(
                country = country,
                isVisibleCodeField = false,
                error = null
            )
        }
    }
    fun loginScreenOpened(phone: String) {
        viewModelScope.launch {
            _uiState.value = ScreenState.LoginScreen(
                phone = phone,
                isVisibleCodeField = false,
                error = null
            )
        }
    }

    fun tryToLogin(phone: String) {
        viewModelScope.launch {
            try {
                phoneNumberUtil.parse(
                    phone,
                    Phonenumber.PhoneNumber.CountryCodeSource.FROM_NUMBER_WITH_PLUS_SIGN.name
                )
                _uiState.value = ScreenState.LoginScreen(
                    phone = phone,
                    isVisibleCodeField = true,
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = ScreenState.LoginScreen(
                    phone = phone,
                    isVisibleCodeField = false,
                    error = e.message
                )
            }
        }
    }

    fun registrationScreenOpened(phone: String) {
        _uiState.value = ScreenState.RegistrationScreen(
            temp = phone
        )
    }

    fun chooseCountryScreenOpened() {
        _uiState.value = ScreenState.ChooseCountryScreen(
            countries = getCountry("")
        )
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


    private fun getCurrentRegion() = getCurrentRegionUseCase.execute()

    companion object {
        private const val DEFAULT_FLAG_EMOJI = "\uD83C\uDDF7\uD83C\uDDFA"
    }
}