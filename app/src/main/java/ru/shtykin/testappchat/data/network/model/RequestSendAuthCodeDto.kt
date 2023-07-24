package ru.shtykin.testappchat.data.network.model

import com.google.gson.annotations.SerializedName

data class RequestSendAuthCodeDto(
    @SerializedName("phone") val phone: String,
)