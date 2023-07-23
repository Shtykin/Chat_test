package ru.shtykin.testappchat.data.model

import com.google.gson.annotations.SerializedName

data class RequestCheckAuthCodeDto(
    @SerializedName("refresh_token") val refreshToken: String,
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("user_id") val userId: Long,
    @SerializedName("is_user_exists") val isUserExist: Boolean,
)