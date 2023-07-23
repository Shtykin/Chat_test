package ru.shtykin.testappchat.data.model

import com.google.gson.annotations.SerializedName

data class UserTokensDto(
    @SerializedName("refresh_token") val refreshToken: String,
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("user_id") val userId: Long,
)