package ru.shtykin.testappchat.data.network.model

import com.google.gson.annotations.SerializedName

data class RequestDetailDto(
    @SerializedName("message") val message: String,
)