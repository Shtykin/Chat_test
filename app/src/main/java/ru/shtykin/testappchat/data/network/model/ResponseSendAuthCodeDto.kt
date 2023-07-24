package ru.shtykin.testappchat.data.network.model

import com.google.gson.annotations.SerializedName

data class ResponseSendAuthCodeDto(
    @SerializedName("is_success") val isSuccess: Boolean,
)