package ru.shtykin.testappchat.data.network.model

import com.google.gson.annotations.SerializedName

data class RequestDetailDto(
//    @SerializedName("msg") val msg: String,
    @SerializedName("message") val message: String,
)