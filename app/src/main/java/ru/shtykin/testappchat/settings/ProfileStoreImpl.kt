package ru.shtykin.testappchat.settings

import android.content.SharedPreferences

class ProfileStoreImpl(private val sharedPreferences: SharedPreferences) : ProfileStore {

    override var name: String
        get() = getStringPref(NAME_KEY)
        set(value) { putStringPref(NAME_KEY, value) }

    override var username: String
        get() = getStringPref(USERNAME_KEY)
        set(value) { putStringPref(USERNAME_KEY, value) }

    override var birthday: String
        get() = getStringPref(BIRTHDAY_KEY)
        set(value) { putStringPref(BIRTHDAY_KEY, value) }

    override var city: String
        get() = getStringPref(CITY_KEY)
        set(value) { putStringPref(CITY_KEY, value) }

    override var avatar: String
        get() = getStringPref(AVATAR_KEY)
        set(value) { putStringPref(AVATAR_KEY, value) }

    private fun getStringPref(key: String): String =
        if (sharedPreferences.contains(key)) sharedPreferences.getString(key, "") ?: ""
        else ""

    private fun putStringPref(key: String, value: String) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    companion object {
        private const val NAME_KEY = "AuthStore.phone"
        private const val USERNAME_KEY = "AuthStore.accessToken"
        private const val BIRTHDAY_KEY = "AuthStore.refreshToken"
        private const val CITY_KEY = "AuthStore.city"
        private const val AVATAR_KEY = "AuthStore.avatar"
    }
}