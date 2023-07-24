package ru.shtykin.testappchat.settings

import android.content.SharedPreferences

class AuthStoreImpl(private val sharedPreferences: SharedPreferences) : AuthStore {

    override var phone: String
        get() = getStringPref(PHONE_KEY)
        set(value) { putStringPref(PHONE_KEY, value) }

    override var accessToken: String
        get() = getStringPref(ACCESS_TOKEN_KEY)
        set(value) { putStringPref(ACCESS_TOKEN_KEY, value) }

    override var refreshToken: String
        get() = getStringPref(REFRESH_TOKEN_KEY)
        set(value) { putStringPref(REFRESH_TOKEN_KEY, value) }

    override fun isAuthenticated(): Boolean {
        return accessToken.isNotEmpty()
    }

    override fun clearCredentials(): Boolean = sharedPreferences
        .edit()
        .clear()
        .commit()

    private fun getStringPref(key: String): String =
        if (sharedPreferences.contains(key)) sharedPreferences.getString(key, "") ?: ""
        else ""

    private fun putStringPref(key: String, value: String) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    companion object {
        private const val PHONE_KEY = "AuthStore.phone"
        private const val ACCESS_TOKEN_KEY = "AuthStore.accessToken"
        private const val REFRESH_TOKEN_KEY = "AuthStore.refreshToken"
    }
}