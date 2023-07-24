package ru.shtykin.testappchat.data.network.model

import com.google.gson.annotations.SerializedName

data class RequestCheckAuthCodeDto(
    @SerializedName("phone") val phone: String,
    @SerializedName("code") val code: String,
)