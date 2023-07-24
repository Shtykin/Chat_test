package ru.shtykin.testappchat.domain

import ru.shtykin.testappchat.domain.entity.Profile
import ru.shtykin.testappchat.domain.entity.UserTokens

interface Repository {
    suspend fun registration(phone: String, name: String, userName: String): UserTokens
    suspend fun sendAuthCode(phone: String): Boolean
    suspend fun checkAuthCode(phone: String, code: String): UserTokens
    suspend fun getProfile(): Profile
    suspend fun putProfile(
        name: String,
        birthday: String,
        city: String,
        vk: String,
        instagram: String,
        status: String,
        avatarFilename: String,
        avatarBase64: String,
    )
}