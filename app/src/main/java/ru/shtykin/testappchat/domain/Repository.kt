package ru.shtykin.testappchat.domain

import ru.shtykin.testappchat.domain.entity.Profile

interface Repository {
    suspend fun registration(phone: String, name: String, userName: String)
    suspend fun getSmsCode(phone: String): Boolean
    suspend fun checkAuthCode(phone: String, code: String): Boolean
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