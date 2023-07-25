package ru.shtykin.testappchat.data.network.model

import com.google.gson.annotations.SerializedName

data class RequestRefreshTokenDto(
    @SerializedName("refresh_token") val refreshToken: String,
)