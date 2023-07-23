package ru.shtykin.testappchat.settings

interface AuthStore {
    var phone: String
    var accessToken: String
    var refreshToken: String
    fun isAuthenticated(): Boolean
    fun clearCredentials(): Boolean
}