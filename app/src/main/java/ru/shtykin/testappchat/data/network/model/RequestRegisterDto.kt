package ru.shtykin.testappchat.data.network.model

import com.google.gson.annotations.SerializedName
import retrofit2.http.Field

data class RequestRegisterDto(
    @SerializedName("phone") val phone: String,
    @SerializedName("name") val name: String,
    @SerializedName("username") val username: String,
)