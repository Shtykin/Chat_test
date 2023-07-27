package ru.shtykin.testappchat.domain

import android.graphics.Bitmap
import ru.shtykin.testappchat.domain.entity.Profile
import ru.shtykin.testappchat.domain.entity.UserTokens

interface Repository {
    suspend fun registration(phone: String, name: String, userName: String): UserTokens
    suspend fun sendAuthCode(phone: String): Boolean
    suspend fun checkAuthCode(phone: String, code: String): UserTokens
    suspend fun getProfile(): Profile
    suspend fun putProfile(profile: Profile): Boolean
    suspend fun getBitmapFromUrl(url: String): Bitmap
}